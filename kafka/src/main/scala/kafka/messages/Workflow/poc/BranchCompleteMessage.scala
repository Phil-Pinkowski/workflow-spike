/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package Workflow.poc

import scala.annotation.switch

case class BranchCompleteMessage(var id: String, var branch: String, var data: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "", "")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        id
      }.asInstanceOf[AnyRef]
      case 1 => {
        branch
      }.asInstanceOf[AnyRef]
      case 2 => {
        data
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.id = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.branch = {
        value.toString
      }.asInstanceOf[String]
      case 2 => this.data = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = BranchCompleteMessage.SCHEMA$
}

object BranchCompleteMessage {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"BranchCompleteMessage\",\"namespace\":\"Workflow.poc\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"branch\",\"type\":\"string\"},{\"name\":\"data\",\"type\":\"string\"}]}")
}