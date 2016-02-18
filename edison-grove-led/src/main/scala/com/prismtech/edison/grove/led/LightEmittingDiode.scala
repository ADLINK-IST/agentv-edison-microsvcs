package com.prismtech.edison.grove.led

import java.util.concurrent.atomic.AtomicBoolean

import com.prismtech.agentv.Microsvc
import com.prismtech.edison.led.prelude._
import com.prismtech.edison.led.types._

import dds._
import dds.prelude._
import org.omg.dds.pub.Publisher
import org.omg.dds.sub.Subscriber

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import upm_grove.GroveLed
import scala.collection.JavaConversions._

import io.nuvo.runtime.Config.Logger

class LightEmittingDiode extends Microsvc {
  val logger = new Logger("LED")
  val running = new AtomicBoolean(false)
  var ledState: Option[HardState[LED]] = None

  var ledListenerId: Option[Int] = None
  var lcdColorListenerId: Option[Int] = None

  var led: Option[GroveLed] = None

  // var lcd: Option[Jhd1313m1] = None

  /**
    *  The command line for this microsvc are as follows:
    *
    *   <DigitalInput>
    *
    * @param args the command line args
    */
  override def init(rpub: Publisher, rsub: Subscriber, args: Array[String]): Unit = {
    implicit val (pub, sub)= (rpub, rsub)
    ledState = Some(HardState[LED](LEDTopicName, Durability.TransientLocal))
    val id = args(0).toInt
    led = Some(new GroveLed(id))
  }

  override def stop(): Unit = {
    if (running.getAndSet(false)) {
      led.foreach(l => {
        l.off()
        l.delete()
      })
    }

  }

  override def close(): Unit = {
    if (!running.get()) {
      for {
        led <- ledState
        llid <- ledListenerId
      } yield led.reader.deaf(llid)

      led = None
    } else throw new IllegalStateException("Cannot close a running microsvc")
  }

  override def start(): Boolean = {
    logger.log("Starting LED")
    if (!running.getAndSet(true)) {

      logger.log("Testing LED, it should tun on and off a few times")
      for (i <- 0 to 5) {
        led.foreach(l => l.on())
        Thread.sleep(1000)
        led.foreach(l => l.off())
      }
      logger.log("If the led did not turn on it may be burned!")

      ledState.foreach( ls => {
        ls.reader.listen {
          case DataAvailable(_) =>
            ls.reader.read().filter(_.getData != null).map(_.getData)
              .foreach(st => {
                logger.info("On = " + st.on)
                led.foreach(l => {
                  if (st.on) l.on() else l.off()
                })
              })
        }
      })

    }
    false
  }
}
