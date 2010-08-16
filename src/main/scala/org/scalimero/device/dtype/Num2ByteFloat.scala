package org.scalimero.device.dtype

import org.scalimero.device.dtype._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.dptxlator.DPTXlator._

abstract class Num2ByteFloatValue(override val value: Float) extends DPValue[Float] {
  val unit : String = ""
  val min = -670760
  val max = 670760

  if(value < min || value > max)
    throw new OutOfBoundsException(value.toString, min.toString + " " + unit, max.toString + " " + unit)

  override def toString = value.toString + " " + unit
}

abstract class Num2ByteFloatType[T <: DPValue[Float]](dpt: DPT) extends DPType[T, Float](dpt: DPT) {
    val dptx = new DPTXlator2ByteFloat (dpt)

  def translate(value: String): Float = {
      dptx.setValue(value)
      dptx.getValueFloat
  }

  def translate(value: Float): String = {
      dptx.setValue(value)
      dptx.getValue
  }

  def translate (value: Array[Byte]): String = {
    dptx.setData(value)
    dptx.getValue
  }
}

package object num2ByteFloat {
  trait implicits {
    implicit def float2AIR_PRESSURE(f : Float) = new AIR_PRESSURE(f)
    implicit def float2AIRQUALITY(f : Float) = new AIRQUALITY(f)
    implicit def float2ELECTRICAL_CURRENT(f : Float) = new ELECTRICAL_CURRENT(f)
    implicit def float2HUMIDITY(f : Float) = new HUMIDITY(f)
    implicit def float2INTENSITY_OF_LIGHT(f : Float) = new INTENSITY_OF_LIGHT(f)
    implicit def float2KELVIN_PER_PERCENT(f : Float) = new KELVIN_PER_PERCENT(f)
    implicit def float2POWER(f : Float) = new POWER(f)
    implicit def float2POWERDENSITY(f : Float) = new POWERDENSITY(f)
    implicit def float2TEMPERATURE(f : Float) = new TEMPERATURE(f)
    implicit def float2TEMPERATURE_DIFFERENCE(f : Float) = new TEMPERATURE_DIFFERENCE(f)
    implicit def float2TEMPERATURE_GRADIENT(f : Float) = new TEMPERATURE_GRADIENT(f)
    implicit def float2TIME_DIFFERENCE1(f : Float) = new TIME_DIFFERENCE1(f)
    implicit def float2TIME_DIFFERENCE2(f : Float) = new TIME_DIFFERENCE2(f)
    implicit def float2VOLTAGE(f : Float) = new VOLTAGE(f)
    implicit def float2WIND_SPEED(f : Float) = new WIND_SPEED(f)
  }

  object AIR_PRESSURE extends Num2ByteFloatType[AIR_PRESSURE](DPTXlator2ByteFloat.DPT_AIR_PRESSURE)
  object AIRQUALITY extends Num2ByteFloatType[AIRQUALITY](DPTXlator2ByteFloat.DPT_AIRQUALITY)
  object ELECTRICAL_CURRENT extends Num2ByteFloatType[ELECTRICAL_CURRENT](DPTXlator2ByteFloat.DPT_ELECTRICAL_CURRENT)
  object HUMIDITY extends Num2ByteFloatType[HUMIDITY](DPTXlator2ByteFloat.DPT_HUMIDITY)
  object INTENSITY_OF_LIGHT extends Num2ByteFloatType[INTENSITY_OF_LIGHT](DPTXlator2ByteFloat.DPT_INTENSITY_OF_LIGHT)
  object KELVIN_PER_PERCENT extends Num2ByteFloatType[KELVIN_PER_PERCENT](DPTXlator2ByteFloat.DPT_KELVIN_PER_PERCENT)
  object POWER extends Num2ByteFloatType[POWER](DPTXlator2ByteFloat.DPT_POWER)
  object POWERDENSITY extends Num2ByteFloatType[POWERDENSITY](DPTXlator2ByteFloat.DPT_POWERDENSITY)
  object TEMPERATURE extends Num2ByteFloatType[TEMPERATURE](DPTXlator2ByteFloat.DPT_TEMPERATURE)
  object TEMPERATURE_DIFFERENCE extends Num2ByteFloatType[TEMPERATURE_DIFFERENCE](DPTXlator2ByteFloat.DPT_TEMPERATURE_DIFFERENCE)
  object TEMPERATURE_GRADIENT extends Num2ByteFloatType[TEMPERATURE_GRADIENT](DPTXlator2ByteFloat.DPT_TEMPERATURE_GRADIENT)
  object TIME_DIFFERENCE1 extends Num2ByteFloatType[TIME_DIFFERENCE1](DPTXlator2ByteFloat.DPT_TIME_DIFFERENCE1)
  object TIME_DIFFERENCE2 extends Num2ByteFloatType[TIME_DIFFERENCE2](DPTXlator2ByteFloat.DPT_TIME_DIFFERENCE2)
  object VOLTAGE extends Num2ByteFloatType[VOLTAGE](DPTXlator2ByteFloat.DPT_VOLTAGE)
  object WIND_SPEED extends Num2ByteFloatType[WIND_SPEED](DPTXlator2ByteFloat.DPT_WIND_SPEED)


  class AIR_PRESSURE(override val value: Float) extends Num2ByteFloatValue(value) {
    override val min = 0
    override val unit = "Pa"
  }

  class AIRQUALITY(override val value: Float) extends Num2ByteFloatValue(value) {
    override val min = 0
    override val unit = "ppm"
  }

  class ELECTRICAL_CURRENT(override val value: Float) extends Num2ByteFloatValue(value) {
    override val unit = "mA"
  }

  class HUMIDITY(override val value: Float) extends Num2ByteFloatValue(value) {
    override val min = 0
    override val unit = "%"
  }

  class INTENSITY_OF_LIGHT(override val value: Float) extends Num2ByteFloatValue(value) {
    override val min = 0
    override val unit = "lx"
  }

  class KELVIN_PER_PERCENT(override val value: Float) extends Num2ByteFloatValue(value) {
    override val unit = "K/%"
  }

  class POWER(override val value: Float) extends Num2ByteFloatValue(value) {
    override val unit = "kW"
  }

  class POWERDENSITY(override val value: Float) extends Num2ByteFloatValue(value) {
    override val unit = "W/m²"
  }

  class TEMPERATURE(override val value: Float) extends Num2ByteFloatValue(value) {
    override val min = -273
    override val unit = "°C"
  }

  class TEMPERATURE_DIFFERENCE(override val value: Float) extends Num2ByteFloatValue(value) {
    override val unit = "K"
  }

  class TEMPERATURE_GRADIENT(override val value: Float) extends Num2ByteFloatValue(value) {
    override val unit = "K/h"
  }

  class TIME_DIFFERENCE1(override val value: Float) extends Num2ByteFloatValue(value) {
    override val unit = "s"
  }

  class TIME_DIFFERENCE2
  (override val value: Float) extends Num2ByteFloatValue(value) {
    override val unit = "ms"
  }

  class VOLTAGE(override val value: Float) extends Num2ByteFloatValue(value) {
    override val unit = "mV"
  }

  class WIND_SPEED(override val value: Float) extends Num2ByteFloatValue(value) {
    override val min = 0
    override val unit = "m/s"
  }
}
