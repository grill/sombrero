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
import _root_.net.liftweb.common._
import java.net._
import net.liftweb.http.js._

import org.sombrero.widget._
  
import tuwien.auto.calimero.dptxlator._
/**
 * Generates a Temperature widget
 * @author Gabriel Grill
 */
class Temperature (data: org.sombrero.model.Widget, wp: WidgetPlace) extends StateWidget(data, "analog", wp){
  val knx = KNXTemperature(data.knx().groupAddress.is)
   var isLight = false
   val min:Float = 15
   val max:Float = 30
   val status = 0/*knx.getStatus match{
    	case Full(x:Float)   => ((x-min)/(max-min))*100
    	case _				 => 0
   }*/
   
   properties ++ Map(
//	   "change" -> "function(){" + SHtml.ajaxCall(getTempJsExp, setTemp _)._2 + "}",
	   "clip_front" -> "true",
	   "value"	->	status.toString
   )
   helpUrl = "/helptext/temperature"

   def setTemp(value: String): JsCmd = {
     println("From " + id + " getTemp recieved")
     println("Value: " + value)
     JsRaw(";").cmd
   }
   
   def getTempJsExp(): JsExp = getOption("temp")
   def translate(value: Array[Byte]): String = (((knx.translate(knx.translate(value))-min)/(max-min))*100).toString
   def translate(value: String): String = {
      Log.info("I'm a Temperature tell me what to do");
      knx.translate(if((value.toFloat * 100) < 0) min else (min+((max-min) * value.toFloat)).toFloat)
   }
} 

//This class enables KNX support
case class KNXTemperature(destAddress:String)  
	extends StateKNXWidget [Float](destAddress, "Temperature", 
			TranslatorTypes.TYPE_2OCTET_FLOAT , DPTXlator2ByteFloat.DPT_TEMPERATURE.getID) {
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
