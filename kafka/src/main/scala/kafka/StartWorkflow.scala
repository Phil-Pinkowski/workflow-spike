package kafka

import java.util.Properties
import java.util.UUID.randomUUID

import Workflow.poc.LogMessage
import com.typesafe.scalalogging.LazyLogging
import config.ConfigSettings.{BOOTSTRAP_SERVER, SCHEMA_REGISTRY_URL}
import io.confluent.kafka.serializers.{AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer;

object StartWorkflow extends App with LazyLogging {

  final val START_WORKFLOW_TOPIC = "StartWorkflow"

  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER)
  props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])

  val producer = new KafkaProducer[String, LogMessage](props)

  val message = LogMessage(randomUUID().toString, "Hello, World!")

  val record = new ProducerRecord[String, LogMessage](START_WORKFLOW_TOPIC, message.id, message)

  producer.send(record)

  producer.flush()

}