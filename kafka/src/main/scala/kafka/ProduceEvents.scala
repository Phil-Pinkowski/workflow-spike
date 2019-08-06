package kafka

import java.time.Duration
import java.util.Properties

import Workflow.poc.{EventMessage, LogMessage, WaitForEventMessage}
import com.typesafe.scalalogging.LazyLogging
import config.ConfigSettings.BOOTSTRAP_SERVER
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object ProduceEvents extends App with LazyLogging with AvroImplicits {

  final val BRANCH_A_TOPIC = "BranchA";
  final val BRANCH_B_TOPIC = "BranchB";
  final val BRANCH_A_EVENTS_TOPIC = "BranchAEvents";
  final val BRANCH_B_EVENTS_TOPIC = "BranchBEvents";

  val props = new Properties
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER)
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ProduceEvents")

  val builder = new StreamsBuilder

  val branchA = builder.stream[String, WaitForEventMessage](BRANCH_A_TOPIC)
  val branchB = builder.stream[String, WaitForEventMessage](BRANCH_B_TOPIC)

  branchA.mapValues(message => EventMessage(message.id, "Branch A Event!", "Stuff")).to(BRANCH_A_EVENTS_TOPIC)
  branchB.mapValues(message => EventMessage(message.id, "Branch B Event!", "Stuff")).to(BRANCH_B_EVENTS_TOPIC)

  val streams = new KafkaStreams(builder.build, props)

  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

}
