package com.prismtech.edison.grove.sensor

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

import com.prismtech.agentv.{Duration, PeriodicMicrosvc}
import com.prismtech.edison.sensor.prelude._
import com.prismtech.edison.sensor.types.AnalogSensor

import dds.prelude.SoftState
import org.omg.dds.pub.Publisher
import org.omg.dds.sub.Subscriber

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import upm_gas.TP401

import io.nuvo.runtime.Config.Logger
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

class AirQuality extends PeriodicMicrosvc {
  val logger = new Logger("AirQuality")
  val running = new AtomicBoolean(false)
  val warm    = new AtomicBoolean(false)
  var airQuality: Option[SoftState[AnalogSensor]] = None
  var duration: Option[Duration] = None
  var airQualitySensor: Option[TP401] = None
  var sid: Option[Int] = None
  val warmupTime = 1000 * 180 // 3 mins

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
    airQuality = Some(SoftState[AnalogSensor](AirQualityTopicName))
    sid = Some(args(0).toInt)
    duration = Some(new Duration(args(1).toLong, TimeUnit.MILLISECONDS))
    airQualitySensor = sid.map(new TP401(_))
  }

  override def close(): Unit = {
    if (!running.get()) {
      airQuality.foreach(_.writer.close())
      airQuality = None
      airQualitySensor.foreach(_.delete())
    }
  }

  override def stop(): Unit = {
    running.getAndSet(false)
  }

  override def start(): Boolean  = {
    running.getAndSet(true)
    Future {
      // wait three minutes and then enable the sensor
      Thread.sleep(warmupTime)
      warm.set(true)
    }
    // return false as this microsvc is periodc and does not complete with start.
    false
  }

  override def schedule(): Unit = {
    if (running.get()) {
      if (warm.get()) {
        for {
          aqs <- airQualitySensor
          raw <- Some(aqs.getSample)
          value <- Some(aqs.getPPM)
          id <- sid
          reading <- Some(new AnalogSensor(id.toShort, raw, value))
          aq <- airQuality
        } yield {
          logger.info(s"($raw, $value)")
          aq.writer.write(reading)
        }
      } else {
        logger.info("AirQuality Sensor warming up to stabilise measurements.")
      }
    } else throw new IllegalAccessException("Trying to schedule a stopped microsvc")
  }

  override def getPeriod: Duration = duration.get

}
