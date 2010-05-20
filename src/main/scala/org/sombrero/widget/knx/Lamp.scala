package org.sombrero.widget.knx


import _root_.net.liftweb.http._
import S._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds, JsExp}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.util._

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._
 
import tuwien.auto.calimero.dptxlator._
import scala.concurrent.ops._

class Lamp (data: org.sombrero.model.Widget, wp: WidgetPlace) extends StateWidget(data, "binary", wp){
   val knx = new KNXLamp(data.knx().groupAddress.is)
   var status:Boolean = knx.getStatus match {
     case Full(x: Boolean)	=> x
     case _					=> false
   }
  
   properties ++ Map(
     	"value" -> status.toString
   )  
   helpUrl = "/helptext/lamp"
  
   def translate(value: Array[Byte]): String = knx.translate(knx.translate(value)).toString
   def translate(value: String): String = {
      Log.info("I'm a Lamp tell me what to do");
      knx.translate(! value.toBoolean)
   }
}

class KNXLamp (destAddress:String)  
	extends StateKNXWidget [Boolean](destAddress, "Lamp", 
			TranslatorTypes.TYPE_BOOLEAN, DPTXlatorBoolean.DPT_SWITCH.getID){
	val dptx = new DPTXlatorBoolean (DPTXlatorBoolean.DPT_SWITCH.getID)
 
    def translate (value: Boolean): String = {
      dptx.setValue(value) 
      dptx.getValue
    }
    
    def translate (value: String): Boolean = {
      dptx.setValue(value)
      dptx.getValueBoolean
    }
    
    def translate (value: Array[Byte]): String = {
		dptx.setData(value)
		dptx.getValue
    }   
}
