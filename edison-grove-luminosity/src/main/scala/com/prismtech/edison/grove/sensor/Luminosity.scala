
package com.prismtech.edison.grove.sensor

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

import com.prismtech.agentv.{PeriodicMicrosvc, Duration}
import com.prismtech.edison.sensor.prelude._
import com.prismtech.edison.sensor.types.AnalogSensor

import dds.prelude.SoftState
import org.omg.dds.pub.Publisher
import org.omg.dds.sub.Subscriber

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import io.nuvo.runtime.Config.Logger
import upm_grove.GroveLight


class Luminosity extends PeriodicMicrosvc {
  val logger = new Logger("Luminosity")
  val running = new AtomicBoolean(false)
  var luminosity: Option[SoftState[AnalogSensor]] = None
  var duration: Option[Duration] = None
  var lumSensor: Option[GroveLight] = None
  var sid: Option[Int] = None
  /**
    *  The command line for this microsvc are as follows:
    *
    *   <AnalogInput> <Period-MilliSec>
    *
    * @param rpub publisher provided by the runtime
    * @param rsub subscriber provided by the runtime
    * @param args the command line args
    */
  override def init(rpub: Publisher, rsub: Subscriber, args: Array[String]): Unit = {
    implicit val (pub, sub)= (rpub, rsub)
    luminosity = Some(SoftState[AnalogSensor](LumTopicName))
    sid = Some(args(0).toInt)
    duration = Some(new Duration(args(1).toLong, TimeUnit.MILLISECONDS))
    lumSensor = sid.map(new GroveLight(_))
  }

  override def close(): Unit = {
    if (!running.get()) {
      luminosity.foreach(_.writer.close())
      luminosity = None
      lumSensor.foreach(_.delete())
    }
  }

  override def stop(): Unit = {
    running.getAndSet(false)
  }

  override def start(): Boolean  = {
    running.getAndSet(true)
    // return false as this microsvc is periodc and does not complete with start.
    false
  }

  override def schedule(): Unit = {
    if (running.get()) {
      for {
        ts <- lumSensor
        raw <- Some(ts.raw_value())
        value <- Some(ts.value())
        id <- sid
        reading <- Some(new AnalogSensor(id.toShort, raw, value))
        lum <- luminosity
      } yield {
        logger.info(s"($raw, $value)")
        lum.writer.write(reading)
      }
    } else throw new IllegalAccessException("Trying to schedule a stopped microsvc")
  }

  override def getPeriod: Duration = duration.get

}
