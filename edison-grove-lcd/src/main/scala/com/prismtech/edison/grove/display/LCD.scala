package com.prismtech.edison.grove.display

import java.util.concurrent.atomic.AtomicBoolean

import com.prismtech.agentv.Microsvc
import com.prismtech.edison.display.prelude._
import com.prismtech.edison.display.types._

import dds._
import dds.prelude._
import org.omg.dds.pub.Publisher
import org.omg.dds.sub.Subscriber

import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import upm_i2clcd.Jhd1313m1

import scala.collection.JavaConversions._

import io.nuvo.runtime.Config.Logger

class LCD extends Microsvc {
  val logger = new Logger("LCD")
  val running = new AtomicBoolean(false)
  var lcdText: Option[HardState[LCDText]] = None
  var lcdColor: Option[HardState[LCDColor]] = None

  val (lcdAddr, rgvAddr) =  (0x3E, 0x62)

  var lcdTextListenerId: Option[Int] = None
  var lcdColorListenerId: Option[Int] = None

  var lcd: Option[Jhd1313m1] = None

  /**
    *  The command line for this microsvc are as follows:
    *
    *   <AnalogInput> <Period-MilliSec>
    *
    * @param args the command line args
    */
  override def init(rpub: Publisher, rsub: Subscriber, args: Array[String]): Unit = {
    implicit val (pub, sub)= (rpub, rsub)

    lcdText = Some(HardState[LCDText](LCDTextTopicName, Durability.TransientLocal))
    lcdColor= Some(HardState[LCDColor](LCDColorTopicName, Durability.TransientLocal))
    val id = args(0).toInt
    lcd = Some(new Jhd1313m1(id, lcdAddr, rgvAddr))
  }

  override def stop(): Unit = {
    if (running.getAndSet(false)) {
      lcd.foreach(l => {
        l.clear()
        l.delete()
      })
    }

  }


  override def start(): Boolean = {
    logger.log("Starting LCD")
    if (!running.getAndSet(true)) {

      lcd.foreach(l => l.write(0, 0, "Welcome!"))
      Thread.sleep(5000)
      lcd.foreach(l => l.clear())

      lcdText.foreach( lt => {
        lt.reader.listen {
          case DataAvailable(_) =>
            lt.reader.read().filter(_.getData != null).map(_.getData)
              .foreach(txt => {
                logger.info("(" + txt.row + "," + txt.col + ")" + txt.text)
                lcd.foreach(l => {
                  l.write(txt.row, txt.col, txt.text)
                })
              })
        }
      })

      lcdColor.foreach( lc  => {
        lc.reader.listen {
          case DataAvailable(_) =>
            lc.reader.read().filter(_.getData != null).map(_.getData).foreach(rgb => {
              logger.info("RGB = (" + rgb.r + ", " + rgb.g + "," + rgb.b + ")")
              lcd.foreach(l =>{
                l.setColor(rgb.r, rgb.g, rgb.b)
              })
            })
        }
      })

    }
    false
  }

  // @TODO: Implement close to properly release resources.
  override def close(): Unit = { }
}
