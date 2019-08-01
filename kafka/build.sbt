name := "kafkaTest"

version := "0.1"

scalaVersion := "2.12.8"
scalacOptions += "-Ypartial-unification"
scalacOptions in ThisBuild += "-Xlog-implicits"

resolvers += "confluent-release" at "http://packages.confluent.io/maven/"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging"               % "3.8.0"
libraryDependencies += "ch.qos.logback"             % "logback-classic"              % "1.1.3" % Runtime
libraryDependencies += "com.sksamuel.avro4s"        %% "avro4s-core"                 % "1.9.0"

libraryDependencies += "org.apache.kafka"   % "kafka-clients"         % "2.0.0" exclude ("org.slf4j", "slf4j-log4j12")
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "2.3.0"
libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.3.0"
libraryDependencies += "io.confluent" % "kafka-streams-avro-serde" % "5.3.0"
libraryDependencies += "io.github.azhur" %% "kafka-serde-avro4s" % "0.4.0"
libraryDependencies ++= Seq(
  "com.ovoenergy" %% "fs2-kafka",
  "com.ovoenergy" %% "fs2-kafka-vulcan"
).map(_ % "0.20.0-M2")

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0-M1"



