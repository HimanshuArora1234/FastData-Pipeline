import play.api.libs.json.{Format, JsError, Json, JsResult, JsObject, JsString, JsSuccess, JsValue, Reads, Writes}

import scala.language.implicitConversions

/**
  * Utility methods for the [[model]] package
  */
package object model {

  /**
    * Build a JSON reader for a enumeration
    *
    * @param enum The enumeration
    * @tparam E The enumeration type
    *
    * @return The JSON reader
    */
  implicit def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
    def reads(json: JsValue): JsResult[E#Value] = json match {
      case JsString(s) =>
        try {
          JsSuccess(enum.withName(s))
        } catch {
          case _: NoSuchElementException =>
            JsError(s"Enumeration expected of type ${enum.getClass}, but it does not appear to contain the value $s")
        }
      case _ => JsError("String value expected")
    }
  }

  /**
    * Build a JSON writer for a enumeration
    *
    * @tparam E The enumeration type
    *
    * @return The JSON writer
    */
  implicit def enumWrites[E <: Enumeration]: Writes[E#Value] = {
    new Writes[E#Value] {
      def writes(v: E#Value): JsValue = JsString(v.toString)
    }
  }

  /**
    * Build a JSON format for a enumeration
    *
    * @param enum The enumeration
    * @tparam E The enumeration type
    *
    * @return The JSON format
    */
  implicit def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(enumReads(enum), enumWrites)
  }


}