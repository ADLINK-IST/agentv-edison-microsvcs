package com.prismtech.edison.grove.analytics

import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

import com.prismtech.agentv.Microsvc
import com.prismtech.agentv.prelude._
import com.prismtech.edison.led.prelude._
import com.prismtech.edison.led.types._
import com.prismtech.edison.sensor.prelude._
import com.prismtech.edison.sensor.types._

import dds._
import dds.prelude._
import org.omg.dds.pub.Publisher
import org.omg.dds.sub.Subscriber

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import upm_i2clcd.Jhd1313m1

import scala.collection.JavaConversions._

import io.nuvo.runtime.Config.Logger

class SmartLed extends Microsvc {
  val logger = new Logger("SmartLed")
  val running = new AtomicBoolean(false)
  var luminosity: Option[SoftState[AnalogSensor]] = None
  var led: Option[HardState[LED]] = None
  var threshold = 25
  var luminosityListenerId: Option[Int] = None
  var sensorId: Short = 2
  var isOff = new AtomicBoolean(true)
  var lumListererId: Option[Int] = None

  /**
    *  The command line for this microsvc are as follows:
    *
    *   <node> [id(2)] [threshold(25)]
    *
    * @param args the command line args
    */
  override def init(rpub: Publisher, rsub: Subscriber, args: Array[String]): Unit = {

    val targetNode = args(0)
    if (args.length > 1)
      sensorId = args(1).toShort
    if (args.length > 2)
      threshold = args(2).toInt

    logger.info(s"Initialising SmartLed for $targetNode $sensorId $threshold" )
    logger.info("Turning off Led")
    ledOff(sensorId)

    val microsvcPartition = NodePartition + File.separator + targetNode
    val scope = Scope(microsvcPartition)
    implicit val (pub, sub) = scope ()

    luminosity = Some(SoftState[AnalogSensor](LumTopicName))
    led = Some(HardState[LED](LEDTopicName, Durability.TransientLocal))
  }

  override def stop(): Unit = {
    running.getAndSet(false)
  }

  override def close(): Unit = {
    for {
      l <- luminosity
      lid <- luminosityListenerId
    } yield l.reader.deaf(lid)

    luminosity = None

    led.foreach(_.writer.close())
    led = None
  }

  def ledOn(id: Short): Unit = {
    led.foreach(l => {
        l.writer.write(new LED(id, true))
    })
  }
  def ledOff(id: Short): Unit = {
    led.foreach(l => {
      l.writer.write(new LED(id, false))
    })
  }

  override def start(): Boolean = {
    if (!running.getAndSet(true)) {
      logger.info("Starting SmartLcd")

      luminosity.foreach( lum => {
        val id = lum.reader.listen {
          case DataAvailable(_) =>
            lum.reader.read().filter(_.getData != null).map(_.getData)
              .foreach( l => {
                logger.info(s"Luminosity reading: " + l.value)
                if ((l.value < threshold) && isOff.getAndSet(false)) {
                  logger.info("Turning led on")
                  ledOn(sensorId)
                } else if ((l.value >= threshold) && !isOff.getAndSet(true)) {
                  ledOff(sensorId)
                  logger.info("Turning led off")
                }
              })
        }
        lumListererId = Some(id)
      })
    }
    false
  }
}
