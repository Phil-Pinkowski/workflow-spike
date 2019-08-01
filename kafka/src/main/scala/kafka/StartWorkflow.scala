package kafka

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.functor._
import com.typesafe.scalalogging.LazyLogging
import fs2.kafka._
import fs2.kafka.vulcan._
import kafka.messages.{LogMessage, LogMessageImplicits}
import config.ConfigSettings.BOOTSTRAP_SERVER
import java.util.UUID.randomUUID;

object StartWorkflow extends IOApp with AvroImplicits with LogMessageImplicits with LazyLogging {

  final val START_WORKFLOW_TOPIC = "StartWorkflow"

  def run(args: List[String]): IO[ExitCode] = {

    val producerSettings = ProducerSettings[IO, String, LogMessage]
      .withBootstrapServers(BOOTSTRAP_SERVER)

    val stream =
      producerStream[IO]
        .using(producerSettings)
        .map { _ =>
          val message = LogMessage(randomUUID().toString, "Hello, World!")
          val record = ProducerRecord(START_WORKFLOW_TOPIC, message.id, message)
          ProducerRecords.one(record)
        }.map(records => {
        logger.info(s"Sending Message: ${records}")
        records
      }).through(produce(producerSettings))

    stream.compile.drain.as(ExitCode.Success)
  }
}