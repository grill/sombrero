package org.sombrero.widget.knx

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero._

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
 
import org.sombrero.model._
import org.sombrero.snippet._

import tuwien.auto.calimero.dptxlator._

class Rollo (data: org.sombrero.model.Widget) extends StateWidget(data, "analog"){
  val knx = KNXRollo(data.knx().groupAddress.is)
 // val change = "function(){" + SHtml.ajaxCall(getTempJsExp, setTemp _)._2 + "}"
  
//	   "change" -> change,
  
   properties ++ Map(
	   "frontImg" -> "\"/images/rollo0zu.png\"",
	   "backgroundImg" -> "\"/images/rollo0.png\"",
	   "slideRect" -> "[19, 19, 122, 122]",
	   "reverse" -> "true"
   )

   def setTemp(value: String): JsCmd = {
     println("From " + id + " getTemp recieved")
     println("Value: " + value)
     JsRaw(";").cmd
   }
   
   def getTempJsExp(): JsExp = getOption("temp")
   def translate(value: Array[Byte]): String = knx.translate(knx.translate(value)).toString
   def translate(value: String): String = {
      Log.info("I'm a Rollo tell me what to do");
      value
   }
}

case class KNXRollo(destAddress:String)  
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