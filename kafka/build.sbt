name := "kafkaTest"

version := "0.1"

scalaVersion := "2.12.8"
scalacOptions += "-Ypartial-unification"
scalacOptions in ThisBuild += "-Xlog-implicits"

resolvers += "confluent-release" at "http://packages.confluent.io/maven/"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging"               % "3.8.0"
libraryDependencies += "ch.qos.logback"             % "logback-classic"              % "1.1.3" % Runtime
libraryDependencies += "com.sksamuel.avro4s"        %% "avro4s-core"                 % "3.0.0-RC3"

libraryDependencies += "org.apache.kafka"   % "kafka-clients"         % "2.0.0" exclude ("org.slf4j", "slf4j-log4j12")
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "2.3.0"
libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.3.0"
libraryDependencies += "io.confluent" % "kafka-streams-avro-serde" % "5.3.0"
libraryDependencies += "org.apache.avro" % "avro" % "1.8.2"
libraryDependencies += "com.julianpeeters" %% "avrohugger-core" % "1.0.0-RC18"

avroSpecificScalaSource in Compile := new java.io.File("src/main/scala/kafka/messages")
sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue
watchSources ++= ((avroSourceDirectories in Compile).value ** "*.avsc").get

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0-M1"



