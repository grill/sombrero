package org.scalimero.device.dtype

import org.scalimero.device.dtype._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.dptxlator.DPTXlator._

abstract class Num4ByteUnsignedValue(override val value: Long) extends DPValue[Long] {
  val unit : String = ""
  val min = 0L
  val max = 4294967295L
  
  if(value < min || value > max)
		throw new OutOfBoundsException(value.toString, min.toString + " " + unit, max.toString + " " + unit)
  
  override def toString = unit match {
    case "" => value.toString
    case s : String => value.toString + " " + s
  }
}

abstract class Num4ByteUnsignedType[T <: DPValue[Long]](dpt: DPT) extends DPType[T, Long](dpt: DPT) {
    val dptx = new DPTXlator4ByteUnsigned (dpt)
	
	def translate(value: String): Long = {
      dptx.setValue(value)
      dptx.getValueUnsigned
	}
	
	def translate(value: Long): String = {
      dptx.setValue(value) 
      dptx.getValue
	}
	
    def translate (value: Array[Byte]): String = {
		dptx.setData(value)
		dptx.getValue
    }   
}

package object num4ByteUnsigned {
  trait implicits {
    implicit def int2VALUE_4_UCOUNT(i : Int) = new VALUE_4_UCOUNT(i)
  }
  object VALUE_4_UCOUNT extends Num4ByteUnsignedType[VALUE_4_UCOUNT](DPTXlator4ByteUnsigned.DPT_VALUE_4_UCOUNT)

  class VALUE_4_UCOUNT(override val value : Long) extends Num4ByteUnsignedValue(value) {
    override val unit = "pulses"
  }
}

