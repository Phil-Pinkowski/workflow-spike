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

case class BranchCompleteMessage(id: String, branch: String, data: String)

trait BranchCompleteMessageImplicits {
  implicit val branchCompleteMessageCodec: Codec[BranchCompleteMessage] = Codec.record("BranchCompleteMessage", Some("Workflow.poc")) {
    field =>
      (
        field("id", _.id),
        field("branch", _.branch),
        field("data", _.data)
        ).mapN(BranchCompleteMessage)
  }

  implicit val avroBranchCompleteMessageSettings: AvroSettings[IO, BranchCompleteMessage] =
    AvroSettings {
      SchemaRegistryClientSettings[IO](SCHEMA_REGISTRY_URL)
    }

  implicit val branchCompleteAvroSerde: Serde[BranchCompleteMessage] = {
    val sas = new SpecificAvroSerde[BranchCompleteMessage]
    sas.configure(Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL), false)
    sas
  }
}

