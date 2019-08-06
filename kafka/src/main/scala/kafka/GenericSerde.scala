package kafka

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util

import com.sksamuel.avro4s._
import org.apache.avro.Schema
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class GenericSerde[T >: Null](implicit val schema: Schema, implicit val toRecord: ToRecord[T], implicit val fromRecord: FromRecord[T], implicit val decoder: Decoder[T], implicit val encoder: Encoder[T]) extends Serde[T]
  with Deserializer[T]
  with Serializer[T] {

  override def serializer(): Serializer[T] = this

  override def deserializer(): Deserializer[T] = this

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = Unit

  override def close(): Unit = Unit

  override def deserialize(topic: String, data: Array[Byte]): T = {
    if (data == null) null else {
      val input = AvroInputStream.data[T].from(data).build(schema)
      val result = input.iterator.next()
      input.close()
      result
    }
  }

  override def serialize(topic: String, data: T): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    val output = AvroOutputStream.binary[T].to(baos).build(schema)
    output.write(data)
    output.close()
    baos.toByteArray
  }

}