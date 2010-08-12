package org.sombrero.widget.knx

import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.scala.util.Random
import _root_.net.liftweb.util.Log
import java.net._
import net.liftweb.http.js._
import tuwien.auto.calimero.exception._ 

import tuwien.auto.calimero.dptxlator._
import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._
import org.sombrero.widget._
import scala.concurrent.ops._
/**
 * Generates a Dimmer widget
 * @author Gabriel Grill
 */
class Dimmer(data: org.sombrero.model.Widget, wp: WidgetPlace) extends StateWidget(data, "analog", wp){
	val knx = KNXDimmer(data.knx().groupAddress.is)
  	val status = knx.getStatus match{
  		case Full(x:Short)   => x
  		case _				 => 0 }
  /*spawn {knx.getStatus match{
    case Full(x:Short)   => Log.info(x.toString)
    case _				 => 100
  }}*/
  
   properties ++ Map(
	   "frontImg" -> "\"/images/dim0drag.png\"",
	   "backgroundImg" -> "\"/images/dim0.png\"",
	   "slideRect" -> "[19, 90, 122, 42]",
	   "opacity" -> "\"/images/dim0light.png\"",
	   "value"	->	status.toString
    )
   helpUrl = "/helptext/dimmer"
   
   def translate(value: Array[Byte]): String = knx.translate(knx.translate(value)).toString
   def translate(value: String): String = {
      Log.info("I'm a Dimmer tell me what to do");
      knx.translate(if((value.toFloat * 100) < 0) 0.toShort else (value.toFloat * 100).toShort)
   }
}

//This class enables KNX support
case class KNXDimmer(destAddress:String)  
	extends StateKNXWidget [Short](destAddress, "Dimmer", 
			TranslatorTypes.TYPE_8BIT_UNSIGNED , DPTXlator8BitUnsigned.DPT_SCALING.getID) {
	val dptx = new DPTXlator8BitUnsigned (DPTXlator8BitUnsigned.DPT_SCALING.getID)
	
	def translate (value:Short): String = {
      dptx.setValue(value) 
      dptx.getValue
    }
    
    def translate (value: String):Short = {
      dptx.setValue(value)
      dptx.getValueUnsigned
    }
    
    def translate (value: Array[Byte]): String = {
		dptx.setData(value)
		dptx.getValue
    }   
}
