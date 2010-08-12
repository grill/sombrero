package org.sombrero.widget
 
import _root_.net.liftweb.http._
import S._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds, JsExp}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.scala.collection.mutable.Map
import tuwien.auto.calimero.exception._ 

import org.sombrero.util._
import org.sombrero.snippet._
import org.sombrero.model._
import org.sombrero.comet._
import org.sombrero.model

import tuwien.auto.calimero._  
import tuwien.auto.calimero.link._
import tuwien.auto.calimero.process._
import tuwien.auto.calimero.link.medium._
import tuwien.auto.calimero.datapoint._
import tuwien.auto.calimero.dptxlator._


/**
 * This file contains a set of classes to access a EIB/KNX Network, create and manage widgets
 * All JQuery UI widget properties are explained in their respective files
 * @author Gabriel Grill
 */

/**
 * The id of the div tag that represents the main area
 */
object Container {
	val htmlid = "col3_content"
}

/**
 * These classes decide where a widget is placed on the page
 */
abstract class WidgetPlace
//Admin's Toobox in the Admin Sidebar
case object AdminSideBar extends WidgetPlace
//Favorites widget
case object FavChild extends WidgetPlace
//main area
case object FavParent extends WidgetPlace


abstract class StateWidget(data: model.Widget, widgetType: String, wp: WidgetPlace) 
	extends CommandWidget(data, widgetType, wp) {
     val knx: StateKNXWidget[_]
	
    /* 
     * updates the widget on the client with the new value
     */
	def setValue(value: Array[Byte]) = call("update_value", translate(value)).cmd
 	
    /* translates a value from a KNX/EIB device into an understandable one 
 	 * for the KNX/EIB Devices
 	 */
    def translate(value: Array[Byte]): String
    
    override def update(value: String): JsCmd = {
       val r = super.update(value);
 	   try{
 		   knx.getStatus
      }catch{
        case e: Exception => 
 	  }
 	   r
    }
}
 
abstract class CommandWidget(data: model.Widget, widgetType: String, wp: WidgetPlace) 
	extends Widget(data, widgetType, wp) {
    val knx: KNXWidget[_]
    val change = "function(){" + SHtml.ajaxCall(getValue, update _)._2 + "}"
    properties ++ Map(
	   "change" -> change
	)
    
    //gets the value of the widget from the client
    def getValue(): JsExp = /*JavaScriptHelper.getOption(id, "protowidget", "testtest")*/getOption("value")
    
    //recives a value from the client and sends it to the respective KNX device
    def update(value: String): JsCmd = {
    	Log.info("Value: " + value + "; Recvied from: " + id)
	    knx.write(translate(value))
	    JsRaw(";").cmd
 	}
     
 	/* translates a value from the client into an understandable one
     * for the KNX/EIB Devices
 	 *	
 	 */
    def translate(value: String): String
}

abstract class Widget(val data: model.Widget, widgetType: String, var wp: WidgetPlace) {
    //id of the widget
	var id = widgetType + "_" + data.id.is
    //Map of properties for the respective JQuery UI widget
	val properties: Map[String, String] = Map()
	//parent Tag
	var parent: String = Container.htmlid
	val isFav = Fav.isFav(data)
	//URL which points the helptext of the respective widget if changed in a subclass
	var helpUrl = ""
	//a probperty for the favorite widget, which has to be set to the parentObj if
    //the widget is placed in the favorites widget
	var pob: List[(String,String)] = Nil
 
	wp match {
		case  AdminSideBar 	=>	val copy = "$(\"#FavCh_" + id + "\")"
  			if(isFav) properties ++ Map("copy" -> copy)
  			properties ++ Map("admin_img" -> """["ui-icon-help",
                                       "ui-icon-wrench",
                                       "ui-icon-trash",
                                       "ui-icon-plus"]""")
		case  FavChild		=>	id = "FavCh_" + id
  			parent = Fav.htmlid
		case  FavParent		=>	 val copy = "$(\"#FavCh_" + id + "\")"
			if(isFav) properties ++ Map("copy" -> copy)
		case _ => Nil
	}
 
	if(isFav){
	   pob = List(("parentObj", "$(\"#" + widgetType + "_" + data.id.is + "\")"))
	}
 
	
	def render(): NodeSeq = JavaScriptHelper.createWidget(id, widgetType, properties.toList :::
		List(	("top", data.top.is.toString),
				("left", data.left.is.toString),
				("text", '"' + data.name.is + '"'),
				("stop", "function(){" + SHtml.ajaxCall(getTopJsExp, setTop _)._2 + ";"
           			+ SHtml.ajaxCall(getLeftJsExp, setLeft _)._2 + ";}"),
           		("favorites", "$(\"#" + Fav.htmlid + "\")"),
           		("active", JavaScriptHelper.callback(newFavorite)),
           		("inactive", JavaScriptHelper.callback(delFavorite)),
           		("in_toolbox", JavaScriptHelper.callback(newToolboxitem)),
           		("out_toolbox", JavaScriptHelper.callback(delToolboxitem(Room.current)))
		) ::: admin ::: parentTag ::: isActive ::: pob)
 
 	def newToolboxitem(): JsCmd = {
  		 System.out.println("newToolbox");
  		 data.room(Empty).save
		 JsRaw(";").cmd
	}
  
  	def delToolboxitem(r : Box[model.Room])(): JsCmd = {
  		 System.out.println("delToolbox");
  		 data.room(r).save
		 JsRaw(";").cmd
	}
	def newFavorite(): JsCmd = {
  		 //System.out.println("newFavorite");
  		 Log.info("newFavorite");
		 Fav.add(data)
  		 Distributor ! FavAddMessage(data.id.is)
		 JsRaw(";").cmd
	}

 	def delFavorite(): JsCmd = {
  		 Log.info("delFavorite");
 		 Fav.remove(data)
  		 Distributor ! FavRemMessage(data.id.is)
		 JsRaw(";").cmd
	}
	
	def delWidget() : JsCmd = {
	  Log.info("delWidget")
	  data.delete_!
	  JsRaw(";").cmd
	}
  
 	def setTitle(s:String) = callProto("update_title", s).cmd
	def content(): NodeSeq = Nil
	def callProto(option: String, value:String) = JavaScriptHelper.call(id, "protowidget", option, value)
	def call(option: String, value:String) = JavaScriptHelper.call(id, widgetType, option, value)
	def setOption(option: String, value:String) = JavaScriptHelper.setOption(id, widgetType, option, value)
	def getOption(option: String) = JavaScriptHelper.getOption(id, widgetType, option)
	def css(attr: String) = JavaScriptHelper.css(id, attr)
    def getTopJsExp = css("top")//)getOption("top")
    def getLeftJsExp = css("left")//)getOption("left")
    
	def setLeft(value: String): JsCmd = {
		 println("From " + id + " getLeft recieved")
		 println("Value: " + value)
		 data.left(value.split('.')(0).reverse.drop(2).reverse.toInt).save
		 JsRaw(";").cmd
	}
  
    def setTop(value: String): JsCmd = {
        println("From " + id + " getTop recieved")
        println("Value: " + value)
        data.top(value.split('.')(0).reverse.drop(2).reverse.toInt).save
        JsRaw(";").cmd
    }
    
    def parentTag = List(("parentTag", "$(\"#" + parent +  "\")"))
    def admin = if (User.superUser_?) List(("admin", "$(\"#" + ToolBox.id + "\")"), 
                ("admin_url", "[ \"" + helpUrl + """",
		         "/widget/""" + data.id.is + """" ]"""),
		         ("admin_onClick", """[
		            function(){}, function(){}, """ + JavaScriptHelper.callback(delWidget) + """,
		            function(){}
		         ]""")) else Nil
    def isActive = if(isFav) List(("is_active", "true")) else Nil
    
    //this function adds a widget to the favorits widget
    def addFavCmd(): JsCmd = {
        //JavaScriptHelper.call(Fav.htmlid, "favorites", "append", 
       //JavaScriptHelper.call(widgetType + "_" + data.id.is, "titlebar", "setFav", "true") &
        JsRaw("$('<div id=\"" + id + "\"></div>').appendTo($(\"#" + Container.htmlid + "\"));").cmd &
        JsRaw(JavaScriptHelper.initWidget(id, widgetType, properties.toList :::
		List(	("top", data.top.is.toString),
				("left", data.left.is.toString),
				("text", '"' + data.name.is + '"'),
				("stop", "function(){" + SHtml.ajaxCall(getTopJsExp, setTop _)._2 + ";"
           			+ SHtml.ajaxCall(getLeftJsExp, setLeft _)._2 + ";}"),
           		("favorites", "$(\"#" + Fav.htmlid + "\")"),
           		("active", JavaScriptHelper.callback(newFavorite)),
           		("inactive", JavaScriptHelper.callback(delFavorite)),
           		("in_toolbox", JavaScriptHelper.callback(newToolboxitem)),
           		("out_toolbox", JavaScriptHelper.callback(delToolboxitem(Room.current)))
		) ::: admin ::: parentTag ::: isActive ::: pob)).cmd &
        JavaScriptHelper.call(Fav.htmlid, "favorites", "deactivate_and_append", "$(\"#" + id + "\")")
    }
    
    //this function removes a widget from the favorits widget
    def removeFavCmd(): JsCmd = {
       JavaScriptHelper.call(widgetType + "_" + data.id.is, "titlebar", "setFav", "false") &
       JavaScriptHelper.call(Fav.htmlid, "favorites", "remove", "$(\"#" +"FavCh_" + widgetType + "_" + data.id.is + "\")")
    }
}

abstract class KNXWidget[T](destAddress:String, name:String, mainNumber:Int, dptID:String){
	System.out.println(destAddress);
    val destDevice = new GroupAddress(destAddress)
    val dptx: DPTXlator
    val dp: Datapoint
    
    def translate (value: T): String
	def write (status: T) = if(Connection.isConnected) Connection.knxComm.write(dp, translate(status))
	def write (status: String) = if(Connection.isConnected) Connection.knxComm.write(dp, status)
} 

abstract class StateKNXWidget[T] (destAddress:String, name:String, mainNumber:Int, dptID:String)
		 extends KNXWidget[T](destAddress, name, mainNumber, dptID){
    override val dp = new StateDP(destDevice, name, mainNumber, dptID)
    
    def translate (value: String): T
    def translate (value: Array[Byte]): String
	def getStatus (): Box[T] = {
	  if(Connection.isConnected){
		  Log.info(Connection.knxComm.toString)
		  try {
			  Full(translate(Connection.knxComm.read(dp)))
		  } catch {
		    case e => Empty
		  }
	  }else
         Empty
	}
}

abstract class CommandKNXWidget [T] (destAddress:String, name:String, mainNumber:Int, dptID:String)
		 extends KNXWidget[T] (destAddress, name, mainNumber, dptID){
    override val dp = new CommandDP(destDevice, name, mainNumber, dptID)
}
