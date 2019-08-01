package kafka

import java.util.concurrent.TimeUnit

import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.scalalogging.LazyLogging
import fs2.kafka._
import fs2.kafka.vulcan._
import kafka.messages.{LogMessage, LogMessageImplicits, WaitForEventMessage, WaitForEventMessageImplicits}
import config.ConfigSettings.BOOTSTRAP_SERVER
import cats.implicits._
import StartWorkflow.START_WORKFLOW_TOPIC

import scala.concurrent.duration.FiniteDuration

object Schedule extends IOApp with AvroImplicits with LogMessageImplicits with WaitForEventMessageImplicits with LazyLogging {

  final val BRANCH_A_TOPIC = "BranchA";
  final val BRANCH_B_TOPIC = "BranchB";

  val producerSettings: ProducerSettings[IO, String, WaitForEventMessage] = ProducerSettings[IO, String, WaitForEventMessage]
    .withBootstrapServers(BOOTSTRAP_SERVER)

  val consumerSettings: ConsumerSettings[IO, String, LogMessage] = ConsumerSettings[IO, String, LogMessage]
    .withAutoOffsetReset(AutoOffsetReset.Earliest)
    .withBootstrapServers(BOOTSTRAP_SERVER)
    .withGroupId("LogMessageGroup")

  def logMessageReceived: LogMessage => Unit = (log: LogMessage) => logger.info(s"Recieved Log Message: ${log.id}, ${log.message}")

  def sideEffect[O]: (O => Any) => O => O = (effect: O => Any) => (i: O) => {
    effect(i)
    i
  }

  def createProducerRecord(topic: String, id: String, eventType: String): ProducerRecord[String, WaitForEventMessage] = {
    val message = WaitForEventMessage(id, eventType)
    ProducerRecord(topic, message.id, message)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    producerStream[IO]
      .using(producerSettings)
      .flatMap { _ =>
        consumerStream[IO]
          .using(consumerSettings)
          .evalTap(_.subscribeTo(START_WORKFLOW_TOPIC))
          .flatMap(_.stream)
          .map(c => c.record.value)
          .map(sideEffect(logMessageReceived))
          .map(log => {
            val recordA = createProducerRecord(BRANCH_A_TOPIC, log.id, "BranchAEvent")
            val recordB = createProducerRecord(BRANCH_B_TOPIC, log.id, "BranchBEvent")
            ProducerRecords(List(recordA, recordB))
          })
          // ¯\_(ツ)_/¯
          // "schedule"
          .delayBy(FiniteDuration(10, TimeUnit.SECONDS))
      }
      .through(produce(producerSettings))
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
