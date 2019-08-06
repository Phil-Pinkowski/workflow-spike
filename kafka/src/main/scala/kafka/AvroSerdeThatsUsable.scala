//package kafka
//
//import java.io.ByteArrayOutputStream
//
//import com.sksamuel.avro4s._
//import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}
//
//class AvroByteArraySerializer[T: SchemaFor : ToRecord] extends Serializer[T] {
//  override def serialize(topic: String, data: T): Array[Byte] = {
//    val outputStream = new ByteArrayOutputStream
//    val output = AvroOutputStream.binary[T](outputStream)
//    try {
//      output.write(data)
//      output.flush
//      outputStream.toByteArray
//    } finally output.close
//  }
//}
//
//class AvroByteArrayDeserializer[T: SchemaFor : FromRecord] extends Deserializer[T] {
//  override def deserialize(topic: String, data: Array[Byte]): T = {
//    val inputStream = AvroInputStream.binary[T](data)
//    val result = inputStream.iterator.next
//    inputStream.close
//    result
//  }
//}
//
//class AvroSerdeThatsUsable[T](implicit val schemaFor: SchemaFor[T], implicit val toRecord: ToRecord[T], implicit val fromRecord: FromRecord[T]) extends Serde[T] {
//  val serializer: Serializer[T] = new AvroByteArraySerializer[T]()
//  val deserializer: Deserializer[T] = new AvroByteArrayDeserializer[T]
//}
