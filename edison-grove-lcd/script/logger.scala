import com.prismtech.edison.display.types._
import com.prismtech.edison.display.prelude._
import dds._
import dds.prelude._
import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import com.prismtech.edison.sensor.types._
import scala.collection.JavaConversions._

val p = "com/prismtech/node/edison"

val scope = Scope(p)
implicit val (p, s) = scope ()

val lcdText = HardState[LCDText](LCDTextTopicName, Durability.TransientLocal)
val lcdRgb = HardState[LCDColor](LCDColorTopicName, Durability.TransientLocal)

def setColor(id: Short, r: Short, g: Short, b: Short): Unit = {
  val color = new LCDColor(id, r, g, b)
  lcdRgb.writer.write(color)
}

def setText(id: Short, row: Short, col: Short, txt: String): Unit = {
  val lcdtxt = new LCDText(id, row, col, txt)
  lcdText.writer.write(lcdtxt )
}
  
