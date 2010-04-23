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
import _root_.net.liftweb.util.Log
import java.net._ 
import net.liftweb.http.js._

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._

import tuwien.auto.calimero.dptxlator._
import tuwien.auto.calimero.exception.KNXException._
import tuwien.auto.calimero.link._
 

class Unary (data: org.sombrero.model.Widget) extends CommandWidget(data, "unary"){
   val knx = new KNXSwitch(data.knx.groupAddress.is)
   var status = false//!device.getStatus
   properties ++ Map(
//        "click" -> JavaScriptHelper.callback(change),
        "img" -> "\"/images/Toggle.png\""
   )
   
/*   def change(): JsCmd = {
     println("From " + id + " change recieved")
     try{
    	 device.write(status)
     } catch {
       case cl: Exception => "No Connection availible"
     }
     status = !status
     JsRaw(";").cmd
   }
*/   
   def translate(value: String): String = {
      Log.info("I'm a Switch tell me what to do");
      value
   }
}

class Switch (data: model.Widget) extends Unary(data)

class SwitchOff (data: org.sombrero.model.Widget) extends Unary(data){  
  properties ++ Map("img" -> "\"/images/ButtonOff.png\"")
}
 
class SwitchOn (data: org.sombrero.model.Widget) extends Unary(data){ 
  properties ++ Map("img" -> "\"/images/ButtonOn.png\"")  
}
  
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
