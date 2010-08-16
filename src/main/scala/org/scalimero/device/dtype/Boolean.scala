package org.scalimero.device.dtype

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.dptxlator.DPTXlator._

abstract class BooleanType(dpt: DPT) extends DPType[BooleanValue, Boolean](dpt) {
  val dptx = new DPTXlatorBoolean (dpt.getID)

  def translate(value: String): Boolean = {
    dptx.setValue(value)
    dptx.getValueBoolean
  }

  def translate(value: Boolean): String = {
    dptx.setValue(value) 
    dptx.getValue
  }

  def translate (value: Array[Byte]): String = {
    dptx.setData(value)
    dptx.getValue
  }
}

package object boolean{
  object ACK extends BooleanType(DPTXlatorBoolean.DPT_ACK )
  object ALARM extends BooleanType(DPTXlatorBoolean.DPT_ALARM )
  object BINARYVALUE extends BooleanType(DPTXlatorBoolean.DPT_BINARYVALUE )
  object BOOL extends BooleanType(DPTXlatorBoolean.DPT_BOOL )
  object DIMSENDSTYLE extends BooleanType(DPTXlatorBoolean.DPT_DIMSENDSTYLE)
  object ENABLE extends BooleanType(DPTXlatorBoolean.DPT_ENABLE)
  object INPUTSOURCE  extends BooleanType(DPTXlatorBoolean.DPT_INPUTSOURCE)
  object INVERT extends BooleanType(DPTXlatorBoolean.DPT_INVERT)
  object LOGICAL_FUNTCION extends BooleanType(DPTXlatorBoolean.DPT_LOGICAL_FUNCTION)
  object OCCUPANCY extends BooleanType(DPTXlatorBoolean.DPT_OCCUPANCY)
  object OPENCLOSE extends BooleanType(DPTXlatorBoolean.DPT_OPENCLOSE)
  object RAMP extends BooleanType(DPTXlatorBoolean.DPT_RAMP)
  object RESET extends BooleanType(DPTXlatorBoolean.DPT_RESET)
  object SCENE_AB extends BooleanType(DPTXlatorBoolean.DPT_SCENE_AB)
  object SHUTTER_BLINDS_MODE extends BooleanType(DPTXlatorBoolean.DPT_SHUTTER_BLINDS_MODE)
  object START extends BooleanType(DPTXlatorBoolean.DPT_START)
  object STATE extends BooleanType(DPTXlatorBoolean.DPT_STATE)
  object STEP extends BooleanType(DPTXlatorBoolean.DPT_STEP)
  object SWITCH extends BooleanType(DPTXlatorBoolean.DPT_SWITCH)
  object TRIGGER extends BooleanType(DPTXlatorBoolean.DPT_TRIGGER)
  object UPDOWN extends BooleanType(DPTXlatorBoolean.DPT_UPDOWN)
  object WINDOW_DOOR extends BooleanType(DPTXlatorBoolean.DPT_WINDOW_DOOR)

}

abstract class BooleanValue extends DPValue[Boolean]

class False extends BooleanValue{
  override val value = false
}
object False extends False
class True extends BooleanValue {
  override val value = true
}
object True extends True

//ACK
object no_action extends False
object acknwoledge extends True

//ALARM
object no_alarm extends False
object alarm extends True

//SWITCH
object off extends False
object on extends True

//BINARYVALUE
object low extends False
object high extends True

//ENABLE
object disable extends False
object enable extends True

//RAMP
object no_ramp extends False
object ramp extends True

//STEP
object decrease extends False
object increase extends True

//UPDOWN
object up extends False
object down extends True

//OPENCLOSE
object open extends False
object close extends True

//START
object stop extends False
object start extends True

//STATE
object inactive extends False
object active extends True

//INVERT
object not_inverted extends False
object inverted extends True

//DIMSENDSTYLE
object start_stop extends False
object cyclic extends True

//INPUTSOURCE
object fixed extends False
object calculated extends True

//RESET
//object no_action extends False
object reset extends True

//TRIGGER
//object trigger extends False
object trigger extends True

//OCCUPANCY
object not_occupied extends False
object occupied extends True

//WINDOW_DOOR
object w_closed extends False
object w_open extends True

//LOGICAL_FUNCTION
object OR extends False
object AND extends True

//SCENE_AB
object scene_A extends False
object scene_B extends True

//SHUTTER_BLINDS_MODE
object only_move_up_down extends False
object move_up_down_and_step_stop extends True

//BOOL
/**
 *  primitive types are used in this case
 **/


