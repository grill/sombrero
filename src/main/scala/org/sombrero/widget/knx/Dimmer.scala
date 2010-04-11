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

case class DimmerAdmCopy(data: org.sombrero.model.Widget) extends ProtoDimmer(data, "Fav_Dimmer",Container.htmlid) {
	properties = ("admin_img", """["ui-icon-help",
		                  "ui-icon-wrench",
			              "ui-icon-trash",
			              "ui-icon-plus"]""") :: properties
}  
case class DimmerFavCopy(data: org.sombrero.model.Widget) extends ProtoDimmer(data, "Dimmer",Fav.htmlid)
case class Dimmer(data: org.sombrero.model.Widget) extends ProtoDimmer(data, "Adm_Dimmer",Container.htmlid)
  
class ProtoDimmer(data: org.sombrero.model.Widget, prefix: String, parent: String) extends StateWidget(data, prefix, "analog", parent){
  val knx = KNXDimmer(data.knx().groupAddress.is)
  
   var properties = List(
	   ("change", "function(){" + SHtml.ajaxCall(getTempJsExp, setTemp _)._2 + "}"),
	   ("frontImg", '"' + "/images/dim0drag.png" + '"'),
	   ("backgroundImg", '"' + "/images/dim0.png" + '"'),
	   ("slideRect", "[19, 90, 122, 42]"),
	   ("opacity", "\"/images/dim0light.png\"")
   )

   def setTemp(value: String): JsCmd = {
     println("From " + id + " getTemp recieved")
     println("Value: " + value)
     JsRaw(";").cmd
   }
     
   def getTempJsExp(): JsExp = getOption("temp")
   
   def translate(value: Array[Byte]): String = knx.translate(knx.translate(value)).toString
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