package kafka.messages

import java.util.Collections

import cats.effect.IO
import cats.implicits._
import config.ConfigSettings.SCHEMA_REGISTRY_URL
import fs2.kafka.vulcan._
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.common.serialization.Serde
import vulcan.Codec

final case class WaitForEventMessage(id: String, eventName: String)

trait WaitForEventMessageImplicits {
  implicit val waitForEventMessageCodec: Codec[WaitForEventMessage] = Codec.record("WaitForEventMessage", Some("Workflow.poc")) {
    field =>
      (
        field("id", _.id),
        field("eventName", _.eventName)
        ).mapN(WaitForEventMessage)
  }

  implicit val avroWaitForEventSettings: AvroSettings[IO, WaitForEventMessage] =
    AvroSettings {
      SchemaRegistryClientSettings[IO](SCHEMA_REGISTRY_URL)
    }

  implicit val waitForEventAvroSerde: Serde[WaitForEventMessage] = {
    val sas = new SpecificAvroSerde[WaitForEventMessage]
    sas.configure(Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL), false)
    sas
  }
}
