import com.prismtech.edison.led.types._
import com.prismtech.edison.led.prelude._
import dds._
import dds.prelude._
import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import com.prismtech.edison.sensor.types._
import scala.collection.JavaConversions._


val p = "com/prismtech/node/EDISON-D4-AC"

val scope = Scope(p)
implicit val (p, s) = scope ()

val ledState = HardState[LED](LEDTopicName, Durability.TransientLocal)

def ledOn(id: Short): Unit = {
  ledState.writer.write(new LED(id, true))
}


def ledOff(id: Short): Unit = {
  ledState.writer.write(new LED(id, false))
}
