package kafka

import java.time.Duration
import java.util.Properties

import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.scalalogging.LazyLogging
import config.ConfigSettings.BOOTSTRAP_SERVER
import kafka.Schedule.{BRANCH_A_TOPIC, BRANCH_B_TOPIC}
import kafka.messages.{BranchCompleteMessage, BranchCompleteMessageImplicits, EventMessage, EventMessageImplicits, LogMessageImplicits, WaitForEventMessage, WaitForEventMessageImplicits}
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.ImplicitConversions._

object WaitForBranchAEvent extends IOApp with AvroImplicits with LazyLogging with WaitForEventConsumer {
  val BRANCH_A_EVENTS: String = "BranchAEvents"
  val BRANCH_A_COMPLETED_TOPIC: String = "BranchACompleted"

  def run(args: List[String]): IO[ExitCode] = {
    createWaitForEventConsumer(BRANCH_A_TOPIC, BRANCH_A_EVENTS, BRANCH_A_COMPLETED_TOPIC)
  }
}

object WaitForBranchBEvent extends IOApp with AvroImplicits with LazyLogging with WaitForEventConsumer {
  val BRANCH_B_EVENTS: String = "BranchBEvents"
  val BRANCH_B_COMPLETED_TOPIC: String = "BranchBCompleted"

  def run(args: List[String]): IO[ExitCode] = {
    createWaitForEventConsumer(BRANCH_B_TOPIC, BRANCH_B_EVENTS, BRANCH_B_COMPLETED_TOPIC)
  }
}

trait WaitForEventConsumer extends WaitForEventMessageImplicits with EventMessageImplicits with BranchCompleteMessageImplicits {
  def createWaitForEventConsumer(waitTopic: String, eventTopic: String, resultTopic: String): IO[ExitCode] = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "poc-wait-for-event")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER)

    val builder = new StreamsBuilder()

    val waitingForEvents = builder.stream[String, WaitForEventMessage](waitTopic)
    val events = builder.stream[String, EventMessage](eventTopic)

    waitingForEvents.join(
      events)(
      (waitFor: WaitForEventMessage, event: EventMessage) => BranchCompleteMessage(waitFor.id, waitTopic, event.data),
      JoinWindows.of(Duration.ofDays(1))
    ).to(resultTopic)

    IO.pure(ExitCode.Success)
  }
}

