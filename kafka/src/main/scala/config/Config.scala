package config

case class Config(kafka: Kafka) {}

case class Kafka(schemaRegistryUrl: String, bootstrapServerUrl: String, topic: String)

object ConfigSettings {
  final val SCHEMA_REGISTRY_URL = "http://localhost:8081"
  final val BOOTSTRAP_SERVER = "localhost:9092"
}