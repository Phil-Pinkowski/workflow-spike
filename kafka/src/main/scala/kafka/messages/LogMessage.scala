package kafka.messages

import cats.effect.IO
import cats.implicits._
import config.ConfigSettings.SCHEMA_REGISTRY_URL
import fs2.kafka.vulcan._
import vulcan.Codec

final case class LogMessage(id: String, message: String)

trait LogMessageImplicits {
  implicit val logMessageCodec: Codec[LogMessage] = Codec.record("LogMessage", Some("workflow.poc")) {
    field =>
      (
        field("id", _.id),
        field("message", _.message)
        ).mapN(LogMessage)
  }
  implicit val avroLogMessageSettings: AvroSettings[IO, LogMessage] =
    AvroSettings {
      SchemaRegistryClientSettings[IO](SCHEMA_REGISTRY_URL)
    }
}
