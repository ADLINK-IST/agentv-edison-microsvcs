name            := "edison-grove-temperature"

version		:= "0.5.0-SNAPSHOT"

organization 	:= "com.prismtech"

homepage :=  Some(new java.net.URL("http://prismtech.com"))

scalaVersion 	:= "2.11.7"


packageOptions += Package.ManifestAttributes(
  "Entry-Point" -> "com.prismtech.edison.grove.sensor.Temperature",
  "Jar-Kind"    -> "vortex-microservice"
)


resolvers += "Vortex Snapshot Repo" at "https://dl.dropboxusercontent.com/u/19238968/devel/mvn-repo/vortex"

resolvers += "PrismTech Snapshot Repo"at " http://prismtech.github.io/mvn-repo/snapshots"

resolvers += "nuvo.io maven repo" at "http://nuvo-io.github.com/mvn-repo/snapshots"

resolvers += "Local Repo"at  "file://"+Path.userHome.absolutePath+"/.ivy2/local"

libraryDependencies += "com.prismtech" % "agentv-microsvc_2.11" % "0.5.0-SNAPSHOT"

libraryDependencies += "com.prismtech" % "agentv-prelude_2.11" % "0.5.0-SNAPSHOT"

libraryDependencies += "com.prismtech" % "edison-core-types_2.11" % "0.5.0-SNAPSHOT"

libraryDependencies += "com.prismtech.cafe" % "cafe" % "2.2.1-SNAPSHOT"

libraryDependencies += "io.nuvo" % "moliere_2.11" % "0.12.0-SNAPSHOT"

libraryDependencies += "io.nuvo" % "nuvo-core_2.11" % "0.3.0-SNAPSHOT"

publishTo := Some(Resolver.file("file",  new File( "/Users/veda/hacking/labs/techo/mvn-repo/snapshots" )) )
