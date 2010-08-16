package org.scalimero.device.dtype

import org.scalimero.device.dtype._

import tuwien.auto.calimero.dptxlator._

abstract class DPType[DataPointValueType <: DPValue[PrimitiveType], PrimitiveType](val dpt: DPT) {
  def id = dpt.getID()
  def translate(value: String): PrimitiveType
  def translate(value: PrimitiveType): String
  def translate(value: Array[Byte]): String
}
/*
object DPType{
  implicit def dpt2dptype(dpt: DPT) = new DPType(DPT)
}*/

abstract class DPValue[PrimitiveType] {
  val value: PrimitiveType
}

class OutOfBoundsException(value: String, min: String, max: String) extends Exception("The value = " + value +
  "is out of bounds! Please choose a values from " + min + " to " + max + ".")
