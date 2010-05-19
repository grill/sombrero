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
 
import tuwien.auto.calimero.dptxlator._
import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._

class Dimmer(data: org.sombrero.model.Widget, wp: WidgetPlace) extends StateWidget(data, "analog", wp){
  val knx = KNXDimmer(data.knx().groupAddress.is)
   properties ++ Map(
	   "frontImg" -> "\"/images/dim0drag.png\"",
	   "backgroundImg" -> "\"/images/dim0.png\"",
	   "slideRect" -> "[19, 90, 122, 42]",
	   "opacity" -> "\"/images/dim0light.png\""
    )
   
   def translate(value: Array[Byte]): String = knx.translate(knx.translate(value)).toString
   def translate(value: String): String = {
      Log.info("I'm a Dimmer tell me what to do");
      value
   }
}

case class KNXDimmer(destAddress:String)  
	extends StateKNXWidget [Float](destAddress, "Temperature", 
			TranslatorTypes.TYPE_2OCTET_SIGNED , DPTXlator2ByteFloat.DPT_TEMPERATURE.getID) {
	val dptx = new DPTXlator2ByteFloat (DPTXlator2ByteFloat.DPT_TEMPERATURE.getID)
	
	def translate (value:Float): String = {
      dptx.setValue(value) 
      dptx.getValue
    }
    
    def translate (value: String):Float = {
      dptx.setValue(value)
      dptx.getValueFloat
    }
    
    def translate (value: Array[Byte]): String = {
		dptx.setData(value)
		dptx.getValue
    }   
}
