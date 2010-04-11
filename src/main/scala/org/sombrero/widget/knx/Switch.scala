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
 

class Unary (data: org.sombrero.model.Widget, name:String, parent: String) extends widget.Widget(data, name, "unary", parent){
   val device = new KNXSwitch(data.knx.groupAddress.is)
   var status = false//!device.getStatus
   var properties = List(
        ("click", JavaScriptHelper.callback(change))
   )
   
   def change(): JsCmd = {
     println("From " + id + " change recieved")
     try{
    	 device.write(status)
     } catch {
       case cl: Exception => "No Connection availible"
     }
     status = !status
     JsRaw(";").cmd
   }
}

case class SwitchAdmCopy(data: org.sombrero.model.Widget) extends ProtoSwitch(data, "", Container.htmlid)  {
	properties = ("admin_img", """["ui-icon-help",
		                  "ui-icon-wrench",
			              "ui-icon-trash",
			              "ui-icon-plus"]""") :: properties
}  
case class SwitchFavCopy(data: org.sombrero.model.Widget) extends ProtoSwitch(data, "Fav", Fav.htmlid)
case class Switch(data: org.sombrero.model.Widget) extends ProtoSwitch(data, "Adm", Container.htmlid)

class ProtoSwitch (data: model.Widget, prefix: String, parent:String) extends Unary(data, prefix + "_Switch", parent)

case class SwitchOffAdmCopy(data: org.sombrero.model.Widget) extends ProtoSwitchOff(data, "Adm", Container.htmlid)  {
	properties = ("admin_img", """["ui-icon-help",
		                  "ui-icon-wrench",
			              "ui-icon-trash",
			              "ui-icon-plus"]""") :: properties
}  
case class SwitchOffFavCopy(data: org.sombrero.model.Widget) extends ProtoSwitchOff(data, "Fav", Fav.htmlid)
case class SwitchOff(data: org.sombrero.model.Widget) extends ProtoSwitchOff(data, "", Container.htmlid)

class ProtoSwitchOff (data: org.sombrero.model.Widget, prefix:String, parent: String) extends Unary(data, prefix + "_SwitchOff", parent){  
  properties ::= ("img", "\"/images/ButtonOff.png\"")
}
 
case class SwitchOnAdmCopy(data: org.sombrero.model.Widget) extends ProtoSwitchOn(data, "Adm", Container.htmlid)  {
	properties = ("admin_img", """["ui-icon-help",
		                  "ui-icon-wrench",
			              "ui-icon-trash",
			              "ui-icon-plus"]""") :: properties
}  
case class SwitchOnFavCopy(data: org.sombrero.model.Widget) extends ProtoSwitchOn(data, "Fav", Fav.htmlid)
case class SwitchOn(data: org.sombrero.model.Widget) extends ProtoSwitchOn(data, "", Container.htmlid)

class ProtoSwitchOn (data: org.sombrero.model.Widget, prefix:String, parent:String) extends Unary(data, prefix + "_SwitchOn", parent){ 
  properties ::= ("img", "\"/images/ButtonOff.png\"")  
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
