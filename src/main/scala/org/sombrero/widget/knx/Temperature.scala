package org.sombrero.widget.knx

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._
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
<<<<<<< HEAD:src/main/scala/org/sombrero/widget/knx/Temperature.scala

class Temperature (data: org.sombrero.model.Widget) extends StateWidget(data, "analog"){
   val knx = KNXTemperature(data.knx().groupAddress.is)
   var isLight = false
   
   
   properties ++ Map(
//	   "change" -> "function(){" + SHtml.ajaxCall(getTempJsExp, setTemp _)._2 + "}",
	   "clip_front" -> "true"
=======
 
case class TemperatureAdmCopy(data: org.sombrero.model.Widget) extends ProtoTemperature(data, "Temperature", Container.htmlid)  {
	properties = ("admin_img", """["ui-icon-help",
		                  "ui-icon-wrench",
			              "ui-icon-trash",
			              "ui-icon-plus"]""") :: properties
}    
case class TemperatureFavCopy(data: org.sombrero.model.Widget) extends ProtoTemperature(data, "Fav_Temperature", Fav.htmlid) {
	properties ::= ("is_active", "true")
}
case class Temperature(data: org.sombrero.model.Widget) extends ProtoTemperature(data, "Adm_Temperature", Container.htmlid)

class ProtoTemperature (data: org.sombrero.model.Widget, prefix: String, parent:String) extends StateWidget(data, prefix, "analog", parent){
  val knx = KNXTemperature(data.knx().groupAddress.is)
   var isLight = false
   var properties = List(
	   ("change", "function(){" + SHtml.ajaxCall(getTempJsExp, setTemp _)._2 + "}"),
	   ("clip_front", "true")
>>>>>>> origin/master:src/main/scala/org/sombrero/widget/knx/Temperature.scala
   )

   def setTemp(value: String): JsCmd = {
     println("From " + id + " getTemp recieved")
     println("Value: " + value)
     JsRaw(";").cmd
   }
   
   def getTempJsExp(): JsExp = getOption("temp")
   def translate(value: Array[Byte]): String = knx.translate(knx.translate(value)).toString
<<<<<<< HEAD:src/main/scala/org/sombrero/widget/knx/Temperature.scala
   def translate(value: String): String = {
      Log.info("I'm a Temperature tell me what to do");
      value
   }
=======
>>>>>>> origin/master:src/main/scala/org/sombrero/widget/knx/Temperature.scala
} 

case class KNXTemperature(destAddress:String)  
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
