package org.scalimero.device.dtype

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.dptxlator.DPTXlator._

import java.text.SimpleDateFormat
import java.util.Date

//Not tested
abstract class DateTimeType(override val dpt: DPT) extends DPType[DATETIME, Date](dpt) {
  val dptx = new DPTXlatorDateTime (dpt)

  def translate(value: String): Date = {
    dptx.setValue(value)
    new SimpleDateFormat(dpt.getUnit).parse(dptx.getValue)
  }

  def translate(value: Date): String = {
    dptx.setValue(value.toString)
    dptx.getValue
  }

  def translate (value: Array[Byte]): String = {
    dptx.setData(value)
    dptx.getValue
  }
}

abstract class DateType(override val dpt: DPT) extends DPType[DATE, Date](dpt) {
  val dptx = new DPTXlatorDate (dpt)

  def translate(value: String): Date = {
    dptx.setValue(value)
    new SimpleDateFormat(dpt.getUnit).parse(dptx.getValue)
  }

  def translate(value: Date): String = {
    dptx.setValue(value.toString)
    dptx.getValue
  }

  def translate (value: Array[Byte]): String = {
    dptx.setData(value)
    dptx.getValue
  }
}

abstract class TimeType(override val dpt: DPT) extends DPType[TIME, Date](dpt) {
  val dptx = new DPTXlatorTime (dpt)

  def translate(value: String): Date = {
    dptx.setValue(value)
    new SimpleDateFormat(dpt.getUnit).parse(dptx.getValue)
  }

  def translate(value: Date): String = {
    dptx.setValue(value.toString)
    dptx.getValue
  }

  def translate (value: Array[Byte]): String = {
    dptx.setData(value)
    dptx.getValue
  }
}

class DATETIME(override val value: Date) extends DPValue[Date]
class DATE(override val value: Date) extends DPValue[Date]
class TIME(override val value: Date) extends DPValue[Date]

package object dateTime{

  trait implicits {
    implicit def sdf2DATETIME(date : Date) = new DATETIME(date)
    implicit def sdf2DATE(date : Date) = new DATE(date)
    implicit def sdf2TIME(date : Date) = new TIME(date)
  }

  object DATETIME extends DateTimeType(DPTXlatorDateTime.DPT_DATE_TIME)
  
  object DATE extends DateType(DPTXlatorDate.DPT_DATE )

  object TIME extends TimeType(DPTXlatorTime.DPT_TIMEOFDAY)
}
