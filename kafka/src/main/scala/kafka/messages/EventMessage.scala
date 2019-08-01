package kafka.messages

import java.util.Collections

import cats.effect.IO
import cats.implicits._
import config.ConfigSettings.SCHEMA_REGISTRY_URL
import fs2.kafka.vulcan.{AvroSettings, SchemaRegistryClientSettings}
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.common.serialization.Serde
import vulcan.Codec

case class EventMessage(id: String, eventName: String, data: String)

trait EventMessageImplicits {
  implicit val eventMessageCodec: Codec[EventMessage] = Codec.record("EventMessage", Some("Workflow.poc")) {
    field =>
      (
        field("id", _.id),
        field("eventName", _.eventName),
        field("data", _.data)
        ).mapN(EventMessage)
  }

  implicit val avroEventSettings: AvroSettings[IO, EventMessage] =
    AvroSettings {
      SchemaRegistryClientSettings[IO](SCHEMA_REGISTRY_URL)
    }

  implicit val eventAvroSerde: Serde[EventMessage] = {
    val sas = new SpecificAvroSerde[EventMessage]
    sas.configure(Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL), false)
    sas
  }
}

