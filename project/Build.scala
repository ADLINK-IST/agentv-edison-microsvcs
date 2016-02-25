import sbt._
import Keys._

object EdisonMicroSvcs extends Build {

  lazy val coreTypes = Project(id ="edison-core-types", base = file("edison-core-types"))

  lazy val airQuality = Project(id = "edison-grove-airquality", base = file("edison-grove-airquality"))

  lazy val lcd =  Project(id = "edison-grove-lcd", base = file("edison-grove-lcd"))

  lazy val led = Project(id = "edison-grove-led", base = file("edison-grove-led"))

  lazy val lum = Project(id = "edison-grove-luminosity", base = file("edison-grove-luminosity"))

  lazy val temp = Project(id = "edison-grove-temperature", base = file("edison-grove-temperature"))

  lazy val smartLed = Project(id = "edison-smartled", base = file("edison-smartled"))

}
