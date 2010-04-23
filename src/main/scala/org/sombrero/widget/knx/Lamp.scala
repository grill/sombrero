package org.sombrero.widget.knx

<<<<<<< HEAD:src/main/scala/org/sombrero/widget/knx/Lamp.scala

import _root_.net.liftweb.http._
import S._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds, JsExp}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.util._
=======
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JE.{JsRaw,Str}
>>>>>>> origin/master:src/main/scala/org/sombrero/widget/knx/Lamp.scala

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.snippet._
 
import tuwien.auto.calimero.dptxlator._

<<<<<<< HEAD:src/main/scala/org/sombrero/widget/knx/Lamp.scala
class Lamp (data: org.sombrero.model.Widget) extends StateWidget(data, "binary"){
   val knx = new KNXLamp(data.knx().groupAddress.is)
   var status:Boolean = false//knx.getStatus 
  
   properties ++ Map(
     	"value" -> status.toString//,
 //       "click" -> JavaScriptHelper.callback(change)
   )  
  /*
=======
 
case class LampAdmCopy(data: org.sombrero.model.Widget) extends ProtoLamp(data, "Lamp", Container.htmlid)  {
	properties = ("admin_img", """["ui-icon-help",
		                            "ui-icon-wrench",
		           		          	"ui-icon-trash",
		           		            "ui-icon-plus"]""") ::
    properties
}  
case class LampFavCopy(data: org.sombrero.model.Widget) extends ProtoLamp(data, "Fav_Lamp", Fav.htmlid)
case class Lamp(data: org.sombrero.model.Widget) extends ProtoLamp(data, "Adm_Lamp", Container.htmlid)

class ProtoLamp (data: org.sombrero.model.Widget, prefix:String, parent: String) extends StateWidget(data, prefix, "binary", parent){
   val knx = new KNXLamp(data.knx().groupAddress.is)
   var status:Boolean = false//knx.getStatus 
  
   var properties = List(
     	("value", status.toString),
        ("click", JavaScriptHelper.callback(change))
   )  
  
>>>>>>> origin/master:src/main/scala/org/sombrero/widget/knx/Lamp.scala
   def change(): JsCmd = {
     status = !status
     println("From " + id + " change recieved")
     if(status) println("Light on") else println("Light off")
     //device.setStatus(status)
     //knx.write(!knx.getStatus)
     //status = !device.getStatus
     setOption("value", knx.getStatus.toString()).cmd
<<<<<<< HEAD:src/main/scala/org/sombrero/widget/knx/Lamp.scala
   }*/
   
   def translate(value: Array[Byte]): String = knx.translate(knx.translate(value)).toString
   def translate(value: String): String = {
      Log.info("I'm a Lamp tell me what to do");
      value
   }
=======
   }
   def translate(value: Array[Byte]): String = knx.translate(knx.translate(value)).toString
>>>>>>> origin/master:src/main/scala/org/sombrero/widget/knx/Lamp.scala
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
