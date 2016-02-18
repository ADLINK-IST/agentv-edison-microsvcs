import dds._
import dds.prelude._
import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}

import com.prismtech.edison.sensor.types._
import scala.collection.JavaConversions._

val p = "com/prismtech/node*"

val scope = Scope(p)
implicit val (p, s) = scope ()

val temperature = SoftState[AnalogSensor]("Temperature")

temperature.reader.listen {
  case DataAvailable(_) =>
    temperature.reader.read()
      .filter(_.getData != null)
      .map(_.getData)
      .foreach(t => println("[Temperature]: (" + t.rvalue + "," + t.value + ")"))
}

  
