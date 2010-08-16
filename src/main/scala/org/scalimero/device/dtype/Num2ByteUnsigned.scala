package org.scalimero.device.dtype

import org.scalimero.device.dtype._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.dptxlator.DPTXlator._

abstract class Num2ByteUnsignedValue(override val value: Int) extends DPValue[Int] {
  val unit : String = ""
  val min = 0
  val max = 65536
  
  if(value < min || value > max)
		throw new OutOfBoundsException(value.toString, min.toString + " " + unit, max.toString + " " + unit)
  
  override def toString = unit match {
    case "" => value.toString
    case s : String => value.toString + " " + s
  }
}

abstract class Num2ByteUnsignedType[T <: DPValue[Int]](dpt: DPT) extends DPType[T, Int](dpt: DPT) {
    val dptx = new DPTXlator2ByteUnsigned (dpt)
	
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

package object num2ByteUnsigned {
  trait implicits {
    implicit def int2BRIGHTNESS(i : Int) = new BRIGHTNESS(i)
    implicit def int2ELECTRICAL_CURRENT(i : Int) = new ELECTRICAL_CURRENT(i)
    implicit def int2PROP_DATATYPE(i : Int) = new PROP_DATATYPE(i)
    implicit def int2TIMEPERIOD(i : Int) = new TIMEPERIOD(i)
    implicit def int2TIMEPERIOD_10(i : Int) = new TIMEPERIOD_10(i)
    implicit def int2TIMEPERIOD_100(i : Int) = new TIMEPERIOD_100(i)
    implicit def int2TIMEPERIOD_HOURS(i : Int) = new TIMEPERIOD_HOURS(i)
    implicit def int2TIMEPERIOD_MIN(i : Int) = new TIMEPERIOD_MIN(i)
    implicit def int2TIMEPERIOD_SEC(i : Int) = new TIMEPERIOD_SEC(i)
    implicit def int2VALUE_2_UCOUNT(i : Int) = new VALUE_2_UCOUNT(i)
  }
  
  object BRIGHTNESS extends Num2ByteUnsignedType[BRIGHTNESS](DPTXlator2ByteUnsigned.DPT_BRIGHTNESS)
  object ELECTRICAL_CURRENT extends Num2ByteUnsignedType[ELECTRICAL_CURRENT](DPTXlator2ByteUnsigned.DPT_ELECTRICAL_CURRENT)
  object PROP_DATATYPE extends Num2ByteUnsignedType[PROP_DATATYPE](DPTXlator2ByteUnsigned.DPT_PROP_DATATYPE)
  object TIMEPERIOD extends Num2ByteUnsignedType[TIMEPERIOD](DPTXlator2ByteUnsigned.DPT_TIMEPERIOD)
  object TIMEPERIOD_10 extends Num2ByteUnsignedType[TIMEPERIOD_10](DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_10)
  object TIMEPERIOD_100 extends Num2ByteUnsignedType[TIMEPERIOD_100](DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_100)
  object TIMEPERIOD_HOURS extends Num2ByteUnsignedType[TIMEPERIOD_HOURS](DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_HOURS)
  object TIMEPERIOD_MIN extends Num2ByteUnsignedType[TIMEPERIOD_MIN](DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_MIN)
  object TIMEPERIOD_SEC extends Num2ByteUnsignedType[TIMEPERIOD_SEC](DPTXlator2ByteUnsigned.DPT_TIMEPERIOD_SEC)
  object VALUE_2_UCOUNT extends Num2ByteUnsignedType[VALUE_2_UCOUNT](DPTXlator2ByteUnsigned.DPT_VALUE_2_UCOUNT)



  class BRIGHTNESS(override val value : Int) extends Num2ByteUnsignedValue(value) {
    override val unit = "lx"
  }

  class ELECTRICAL_CURRENT(override val value : Int) extends Num2ByteUnsignedValue(value) {
    override val unit = "mA"
  }

  class PROP_DATATYPE(override val value : Int) extends Num2ByteUnsignedValue(value) {
    override val unit = ""
  }

  class TIMEPERIOD(override val value : Int) extends Num2ByteUnsignedValue(value) {
    override val unit = "ms"
  }

  class TIMEPERIOD_10(override val value : Int) extends Num2ByteUnsignedValue(value) {
    override val max = 655350
    override val unit = "ms"
  }

  class TIMEPERIOD_100(override val value : Int)  extends Num2ByteUnsignedValue(value) {
    override val max = 6553500
    override val unit = "ms"
  }

  class TIMEPERIOD_HOURS(override val value : Int) extends Num2ByteUnsignedValue(value) {
    override val unit = "h"
  }

  class TIMEPERIOD_MIN(override val value : Int) extends Num2ByteUnsignedValue(value) {
    override val unit = "min"
  }

  class TIMEPERIOD_SEC(override val value : Int) extends Num2ByteUnsignedValue(value) {
    override val unit = "s"
  }

  class VALUE_2_UCOUNT(override val value : Int) extends Num2ByteUnsignedValue(value) {
    override val unit = "pulses"
  }
}

