/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package Workflow.poc

import scala.annotation.switch

case class WaitForEventMessage(var id: String, var eventName: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        id
      }.asInstanceOf[AnyRef]
      case 1 => {
        eventName
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.id = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.eventName = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = WaitForEventMessage.SCHEMA$
}

object WaitForEventMessage {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"WaitForEventMessage\",\"namespace\":\"Workflow.poc\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"eventName\",\"type\":\"string\"}]}")
}