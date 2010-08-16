package org.sombrero.widget.knx

import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.scala.util.Random
import _root_.net.liftweb.common._
import _root_.net.liftweb.util.Log
import java.net._ 
import net.liftweb.http.js._

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.model
import org.sombrero.snippet._
import org.sombrero.widget._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.exception.KNXException._
import tuwien.auto.calimero.link._
 
/**
 * Generates a Unary Widget
 * @author Gabriel Grill
 */
class Unary (data: org.sombrero.model.Widget, wp: WidgetPlace) extends CommandWidget(data, "unary", wp){
   val knx = new KNXSwitch(data.knx.groupAddress.is)
   var status = false//!device.getStatus
   properties ++ Map(
        "img" -> "\"/images/Toggle.png\""
   )
   helpUrl = "/helptext/switch"
   
   def translate(value: String): String = {
      Log.info("I'm a Switch tell me what to do");
      knx.translate(value.toBoolean)
   }
}
//Generates a Switch widget
class Switch (data: model.Widget, wp: WidgetPlace) extends Unary(data, wp)

//Generates a SwitchOff widget
class SwitchOff (data: org.sombrero.model.Widget, wp: WidgetPlace) extends Unary(data, wp){  
  properties ++ Map("img" -> "\"/images/ButtonOff.png\"")
}
 
//Generates a SwitchOn widget
class SwitchOn (data: org.sombrero.model.Widget, wp: WidgetPlace) extends Unary(data, wp){ 
  properties ++ Map("img" -> "\"/images/ButtonOn.png\"")  
}

//This class enables KNX support
class KNXSwitch(destAddress:String)  
	extends CommandKNXWidget[Boolean] (destAddress, "Switch", 
			TranslatorTypes.TYPE_BOOLEAN, DPTXlatorBoolean.DPT_SWITCH.getID){
	val dptx = new DPTXlatorBoolean (DPTXlatorBoolean.DPT_SWITCH.getID)
	  
    def translate (value:Boolean): String = {
      dptx.setValue(value) 
      dptx.getValue
    }
    
    def translate (value: Array[Byte]): String = {
		dptx.setData(value)
		dptx.getValue
    }   
}
