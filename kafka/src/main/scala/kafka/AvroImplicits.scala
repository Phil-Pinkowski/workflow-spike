package kafka

import java.time.{Instant, LocalDate, LocalDateTime, ZoneOffset}

import com.sksamuel.avro4s.{FromValue, ToSchema, ToValue}
import org.apache.avro.Schema.{Field, Type}
import org.apache.avro.{LogicalTypes, Schema}

@SuppressWarnings(Array("org.wartremover.warts.Throw"))
trait AvroImplicits {

  final val bootstrapServer = "localhost:9092";

  implicit object LocalDateTimeToSchema extends ToSchema[LocalDateTime] {
    protected val schema: Schema = LogicalTypes.timestampMillis().addToSchema(Schema.create(Type.LONG))
  }

  implicit val localDateTimeToValue: ToValue[LocalDateTime] = new ToValue[LocalDateTime] {
    override def apply(i: LocalDateTime): Any = i.toInstant(ZoneOffset.UTC).toEpochMilli
  }

  implicit val localDateTimeFromValue: FromValue[LocalDateTime] = (value: Any, _: Field) => value match {
    case epoch: Long => Instant.ofEpochMilli(epoch).atZone(ZoneOffset.UTC).toLocalDateTime
    case _ => throw new RuntimeException(s"Expected a long representation of an Instant but instead got $value")
  }

  implicit object LocalDateToSchema extends ToSchema[LocalDate] {
    protected val schema: Schema = LogicalTypes.date().addToSchema(Schema.create(Type.INT))
  }

  implicit val localToValue: ToValue[LocalDate] = new ToValue[LocalDate] {
    override def apply(i: LocalDate): Any = i.toEpochDay
  }

  implicit val localFromValue: FromValue[LocalDate] = (value: Any, _: Field) => value match {
    case days: Int => LocalDate.ofEpochDay(days)
    case _ => throw new RuntimeException(s"Expected a long representation of an Instant but instead got $value")
  }
}