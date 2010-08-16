package org.scalimero.device.dtype

import org.scalimero.device.dtype._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.dptxlator.DPTXlator._

abstract class Num3BitControlledValue(override val value: Int) extends DPValue[Int] {
  val unit : String = ""
  val min = -7
  val max = 7
  
  if(value < min || value > max)
		throw new OutOfBoundsException(value.toString, min.toString + " " + unit, max.toString + " " + unit)
  
  override def toString = unit match {
    case "" => value.toString
    case s : String => value.toString + " " + s
  }
}

abstract class Num3BitControlledType[T <: DPValue[Int]](dpt: DPT) extends DPType[T, Int](dpt: DPT) {
  val dptx = new DPTXlator3BitControlled(dpt)
	
	def translate(value: String): Int = {
    dptx.setValue(value)
    dptx.getValueSigned
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

package object num3BitControlled {
  trait implicits {
    implicit def int2CONTROL_BLINDS(i : Int) = new CONTROL_BLINDS(i)
    implicit def int2CONTROL_DIMMING(i : Int) = new CONTROL_DIMMING(i)
  }
  
  object CONTROL_BLINDS extends Num3BitControlledType[CONTROL_BLINDS](DPTXlator3BitControlled.DPT_CONTROL_BLINDS)
    object CONTROL_DIMMING extends Num3BitControlledType[CONTROL_DIMMING](DPTXlator3BitControlled.DPT_CONTROL_DIMMING)
  
  class CONTROL_BLINDS(override val value : Int) extends Num3BitControlledValue(value) {
    
  }
  
  class CONTROL_DIMMING(override val value : Int) extends Num3BitControlledValue(value) {
    
  }
}

