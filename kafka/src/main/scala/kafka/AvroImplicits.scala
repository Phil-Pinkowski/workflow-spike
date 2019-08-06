package kafka

import java.util.Collections

import Workflow.poc.{BranchCompleteMessage, EventMessage, LogMessage, WaitForEventMessage}
import config.ConfigSettings.SCHEMA_REGISTRY_URL
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde

trait AvroImplicits {
  val valueSerdeConfig = Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL)

  implicit val logMessageSerde: SpecificAvroSerde[LogMessage] = new SpecificAvroSerde[LogMessage]
  logMessageSerde.configure(valueSerdeConfig, false)

  implicit val waitForEventMessageSerde: SpecificAvroSerde[WaitForEventMessage] = new SpecificAvroSerde[WaitForEventMessage]
  waitForEventMessageSerde.configure(valueSerdeConfig, false)

  implicit val eventMessageSerde: SpecificAvroSerde[EventMessage] = new SpecificAvroSerde[EventMessage]
  eventMessageSerde.configure(valueSerdeConfig, false)

  implicit val branchCompleteMessageSerde: SpecificAvroSerde[BranchCompleteMessage] = new SpecificAvroSerde[BranchCompleteMessage]
  branchCompleteMessageSerde.configure(valueSerdeConfig, false)

}