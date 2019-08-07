package kafka

import java.time.Duration
import java.util.Properties

import Workflow.poc.{BranchCompleteMessage, EventMessage, WaitForEventMessage}
import com.typesafe.scalalogging.LazyLogging
import config.ConfigSettings.BOOTSTRAP_SERVER
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object WaitForEvent extends App with LazyLogging with AvroImplicits {

  def waitForEvents(builder: StreamsBuilder, waitTopic: String, eventTopic: String, resultTopic: String): Unit = {
    val waitForAEvents = builder.stream[String, WaitForEventMessage](waitTopic)
    val aEvents = builder.stream[String, EventMessage](eventTopic)

    waitForAEvents.join(
      aEvents)(
      (waitFor: WaitForEventMessage, event: EventMessage) => BranchCompleteMessage(waitFor.id, waitTopic, event.data),
      JoinWindows.of(Duration.ofDays(1))
    ).to(resultTopic)
  }

  final val BRANCH_A_TOPIC = "BranchA";
  final val BRANCH_B_TOPIC = "BranchB";
  final val BRANCH_A_EVENTS_TOPIC = "BranchAEvents";
  final val BRANCH_B_EVENTS_TOPIC = "BranchBEvents";
  final val BRANCH_A_COMPLETE_TOPIC = "BranchAComplete"
  final val BRANCH_B_COMPLETE_TOPIC = "BranchBComplete"

  val props = new Properties
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER)
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "WaitForEvents")

  val builder = new StreamsBuilder

  waitForEvents(builder, BRANCH_A_TOPIC, BRANCH_A_EVENTS_TOPIC, BRANCH_A_COMPLETE_TOPIC)
  waitForEvents(builder, BRANCH_B_TOPIC, BRANCH_B_EVENTS_TOPIC, BRANCH_B_COMPLETE_TOPIC)

  val streams = new KafkaStreams(builder.build, props)

  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

}
