package org.scalimero.device.dtype

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.dptxlator.DPTXlator._

//Not tested
abstract class StringType(override val dpt: DPT) extends DPType[STRING, String](dpt) {
  val dptx = new DPTXlatorString (dpt)

  def translate(value: String): String = value
  def translate (value: Array[Byte]): String = {
    dptx.setData(value)
    dptx.getValue
  }
}

class STRING(override val value: String) extends DPValue[String]

package object string {
  trait implicits {
    implicit def string2STRING(s : String) = new STRING(s)
  }
  object ISO_8859_1 extends StringType(DPTXlatorString.DPT_STRING_8859_1)
  object ASCII extends StringType(DPTXlatorString.DPT_STRING_ASCII)
}

