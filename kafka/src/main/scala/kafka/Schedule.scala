package kafka

import java.time.Duration
import java.util.Properties

import Workflow.poc.{LogMessage, WaitForEventMessage}
import com.typesafe.scalalogging.LazyLogging
import config.ConfigSettings.BOOTSTRAP_SERVER
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object Schedule extends App with LazyLogging with AvroImplicits {

  final val BRANCH_A_TOPIC = "BranchA";
  final val BRANCH_B_TOPIC = "BranchB";

  val props = new Properties
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER)
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "Scheduler")

  val builder = new StreamsBuilder

  val startWorkflow = builder.stream[String, LogMessage]("StartWorkflow")

  startWorkflow.mapValues(message => WaitForEventMessage(message.id, "Waiting for Branch A Event!")).to(BRANCH_A_TOPIC)
  startWorkflow.mapValues(message => WaitForEventMessage(message.id, "Waiting for Branch B Event!")).to(BRANCH_B_TOPIC)

  val streams = new KafkaStreams(builder.build, props)

  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

}
