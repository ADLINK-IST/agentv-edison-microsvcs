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

import upm_grove.GroveTemp

class Temperature extends PeriodicMicrosvc {
  val logger = new Logger("Temperature")
  val running = new AtomicBoolean(false)
  var temperature: Option[SoftState[AnalogSensor]] = None
  var duration: Option[Duration] = None
  var tempSensor: Option[GroveTemp] = None
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
    temperature = Some(SoftState[AnalogSensor](TempTopicName))
    sid = Some(args(0).toInt)
    duration = Some(new Duration(args(1).toLong, TimeUnit.MILLISECONDS))
    tempSensor = sid.map(new GroveTemp(_))
  }

  override def close(): Unit = {
    if (!running.get()) {
      temperature.foreach(_.writer.close())
      temperature = None
      tempSensor.foreach(_.delete())
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
        ts <- tempSensor
        raw <- Some(ts.raw_value())
        value <- Some(ts.value())
        id <- sid
        reading <- Some(new AnalogSensor(id.toShort, raw, value))
        temp <- temperature
      } yield {
        logger.info(s"($raw, $value)")
        temp.writer.write(reading)
      }
    } else throw new IllegalAccessException("Trying to schedule a stopped microsvc")
  }

  override def getPeriod: Duration = duration.get

}
