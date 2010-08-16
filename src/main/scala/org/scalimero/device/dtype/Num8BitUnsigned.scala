package org.scalimero.device.dtype

import org.scalimero.device.dtype._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.dptxlator.DPTXlator._

abstract class Num8BitUnsignedValue(override val value: Int) extends DPValue[Int] {
  val unit : String = ""
  val min = 0
  val max = 255
  
  if(value < min || value > max)
		throw new OutOfBoundsException(value.toString, min.toString + " " + unit, max.toString + " " + unit)
  
  override def toString = unit match {
    case "" => value.toString
    case s : String => value.toString + " " + s
  }
}

abstract class Num8BitUnsignedType[T <: DPValue[Int]](dpt: DPT) extends DPType[T, Int](dpt: DPT) {
    val dptx = new DPTXlator8BitUnsigned (dpt)
	
	def translate(value: String): Int = {
      dptx.setValue(value)
      dptx.getValueUnsigned
	}
	
	def translate(value: Int): String = {
      dptx.setValue(value) 
      dptx.getValue
	}
	
    def translate (value: Array[Byte]): String = {
		dptx.setData(value)
		dptx.getValue
    }   
}

package object num8BitUnsigned {
  trait implicits {
    implicit def int2ANGLE(i : Int) = new ANGLE(i)
    implicit def int2DECIMALFACTOR(i : Int) = new DECIMALFACTOR(i)
    implicit def int2PERCENT_U8(i : Int) = new PERCENT_U8(i)
    implicit def int2SCALING(i : Int) = new SCALING(i)
    implicit def int2VALUE_1_UCOUNT(i : Int) = new VALUE_1_UCOUNT(i)
  }
  
  object ANGLE extends Num8BitUnsignedType[ANGLE](DPTXlator8BitUnsigned.DPT_ANGLE)
  object DECIMALFACTOR extends Num8BitUnsignedType[DECIMALFACTOR](DPTXlator8BitUnsigned.DPT_DECIMALFACTOR)
  object PERCENT_U8 extends Num8BitUnsignedType[PERCENT_U8](DPTXlator8BitUnsigned.DPT_PERCENT_U8)
  object SCALING extends Num8BitUnsignedType[SCALING](DPTXlator8BitUnsigned.DPT_SCALING)
  object VALUE_1_UCOUNT extends Num8BitUnsignedType[VALUE_1_UCOUNT](DPTXlator8BitUnsigned.DPT_VALUE_1_UCOUNT)


  class ANGLE(override val value : Int) extends Num8BitUnsignedValue(value) {
    override val max = 360
    override val unit = "°"
  }
  
  class DECIMALFACTOR(override val value : Int) extends Num8BitUnsignedValue(value) {
    override val unit = "ratio"
  }
  
  class PERCENT_U8(override val value : Int) extends Num8BitUnsignedValue(value) {
    override val unit = "%"
  }
  
  class SCALING(override val value : Int) extends Num8BitUnsignedValue(value) {
    override val max = 100
    override val unit = "%"
  }
  
  class VALUE_1_UCOUNT(override val value : Int) extends Num8BitUnsignedValue(value) {
    override val unit = "pulses"
  }
}

