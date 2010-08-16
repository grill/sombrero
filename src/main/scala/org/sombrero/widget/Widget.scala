package org.sombrero.widget
 
import _root_.net.liftweb.http._
import S._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds, JsExp}
import JsCmds._
import JE.{JsRaw,Str}
import _root_.net.liftweb.util.Helpers
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
import tuwien.auto.calimero.exception._

import org.scalimero.device._
import org.scalimero.device.dtype._


/**
 * This file contains a variaty of classes to access EIB/KNX Networks and create and manage widgets
 * All JQuery UI widget properties are explained in their respective Javascript source files
 * @author Gabriel Grill
 */

/**
 * The id of the div tag that represents the main area
 */
object Container {
  val htmlid = "col3_content"
}

/**
 * The subclasses of this class define which container
 * on the webpage is used for storage for the respective widget
 */
abstract class WidgetPlace

//admin's Toobox in the Admin Sidebar
case object AdminSideBar extends WidgetPlace
//favorites widget
case object FavChild extends WidgetPlace
//main area
case object FavParent extends WidgetPlace

abstract class StateWidget[DataPointValueType <: DPValue[PrimitiveType], PrimitiveType](data: model.Widget, widgetType: String, wp: WidgetPlace)
  extends CommandWidget[DataPointValueType, PrimitiveType](data, widgetType, wp) /* with TStateDevice[PrimitiveType]*/{

 /**
  * This method turns a byte value into an Javascript command, that updates
  * the status of a device widget
  */
  def setValue(value: PrimitiveType) = call("update_value", translate(value)).cmd

 /**
  * EIB/KNX device value -> JQuery UI value
  */
  def translate(value: PrimitiveType): String
}

abstract class CommandWidget[DataPointValueType <: DPValue[PrimitiveType], PrimitiveType](data: model.Widget, widgetType: String, wp: WidgetPlace)
  extends Widget(data, widgetType, wp) /* with TCommandDevice[DataPointValueType, PrimitiveType]*/ {
  val knx: Device[DataPointValueType, PrimitiveType]

  properties ~= ("change", "function(){" + SHtml.ajaxCall(getOption("value"), update _)._2 + "}")

 /**
  * This method is used as a callback. When it is called it
  * transforms the String parameter representing a new value
  * for the respective EIB/KNX device into packets, which are
  * then send to the respective devices.
  */
  def update(value: String): JsCmd = {
    Log.info("Value: " + value + "; Recvied from: " + id)
    knx send translate(value)
    Noop
  }

 /**
  * JQuery UI value -> EIB/KNX value
  */
  def translate(value: String): DataPointValueType
}

abstract class Widget(val data: model.Widget, widgetType: String, var wp: WidgetPlace) {
  //id of the widget
  private var id = widgetType + "_" + data.id.is

  //URL which points the helptext of the respective widget if changed in a subclass
  protected lazy val helpUrl = ""

 /**
  * The following part of the constructer is used to set all settings for the
  * JQuery UI widget
  */

  //Map of properties for the respective JQuery UI widget
  var properties = ("top", data.top) ~ ("left", data.left) ~
    ("text", data.name.is) ~
    ("stop", SHtml.ajaxCall(getTopJsExp, setTop _)._2 + ";" +
      SHtml.ajaxCall(getLeftJsExp, setLeft _)._2 + ";") ~
    ("favorites", "#" + Fav.htmlid) ~ ("active", JavaScriptHelper.callback(newFavorite)) ~
    ("inactive", JavaScriptHelper.callback(delFavorite)) ~
    ("in_toolbox", JavaScriptHelper.callback(newToolboxitem)) ~
    ("out_toolbox", JavaScriptHelper.callback(delToolboxitem(Room.current)))

  wp match {
    case  AdminSideBar  =>
      if(Fav.isFav(data)) properties ~= ("copy", "#FavCh_" + id)
      properties ~=
      ("admin_img", JArray("ui-icon-help" :: "ui-icon-wrench" ::
        "ui-icon-trash" :: "ui-icon-plus" :: Nil)) ~
      ("parentTag", "#" + Container.htmlid)
    case  FavChild    =>
      id = "FavCh_" + id
      properties ~= ("parentTag", "#" +  Fav.htmlid)
    case  FavParent   =>
      if(Fav.isFav(data)) properties ~= ("copy", "#FavCh_" + id)
      properties ~= ("parentTag", "#" + Container.htmlid)
    case _ =>
  }
  if(Fav.isFav(data)){
    properties ~= ("parentObj", "#" + widgetType + "_" + data.id.is) ~
      ("is_active", "true")
  }
  if (User.superUser_?) 
    properties ~= ("admin", "#" + ToolBox.id) ~
      ("admin_url", JArray(helpUrl :: "/widget/" + data.id.is :: Nil)) ~
      ("admin_onClick", JArray("" :: "" :: JavaScriptHelper.callback(delWidget) ::
        "" :: Nil))

  def render(): NodeSeq = JavaScriptHelper.createWidget(id, widgetType, properties)

  def newToolboxitem(): JsCmd = {
    Log.info("newToolbox");
    data.room(Empty).save
    Noop
  }

  def delToolboxitem(r : Box[model.Room])(): JsCmd = {
    Log.info("delToolbox");
    data.room(r).save
    Noop
  }

  def newFavorite(): JsCmd = {
    Log.info("newFavorite");
    Fav.add(data)
    Distributor ! FavAddMessage(data.id.is)
    Noop
  }

  def delFavorite(): JsCmd = {
    Log.info("delFavorite");
    Fav.remove(data)
    Distributor ! FavRemMessage(data.id.is)
    Noop
  }

  def delWidget() : JsCmd = {
    Log.info("delWidget")
    data.delete_!
    Noop
  }

  def setTitle(s:String) = callProto("update_title", s).cmd
  def callProto(option: String, value:String) = JavaScriptHelper.call(id, "protowidget", option, value)
  def call(option: String, value:String) = JavaScriptHelper.call(id, widgetType, option, value)
  def setOption(option: String, value:String) = JavaScriptHelper.setOption(id, widgetType, option, value)
  def getOption(option: String) = JavaScriptHelper.getOption(id, widgetType, option)
  def css(attr: String) = JavaScriptHelper.css(id, attr)
  def getTopJsExp = css("top")
  def getLeftJsExp = css("left")

  def setLeft(value: String): JsCmd = {
    Log.info("From " + id + " getLeft recieved")
    Log.info("Value: " + value)
    data.left(value.split('.')(0).reverse.drop(2).reverse.toInt).save
    Noop
  }

  def setTop(value: String): JsCmd = {
    Log.info("From " + id + " getTop recieved")
    Log.info("Value: " + value)
    data.top(value.split('.')(0).reverse.drop(2).reverse.toInt).save
    Noop
  }

  //this function adds a widget to the favorits widget
   def addFavCmd(): JsCmd = {
    //JavaScriptHelper.call(Fav.htmlid, "favorites", "append",
    //JavaScriptHelper.call(widgetType + "_" + data.id.is, "titlebar", "setFav", "true") &
    JsRaw("$('<div id=\"" + id + "\"></div>').appendTo($(\"#" + Container.htmlid + "\"));").cmd &
    JsRaw(JavaScriptHelper.initWidget(id, widgetType, properties)).cmd &
    JavaScriptHelper.call(Fav.htmlid, "favorites", "deactivate_and_append", "$(\"#" + id + "\")")
  }

  //this function removes a widget from the favorits widget
  def removeFavCmd(): JsCmd = {
    JavaScriptHelper.call(id, "titlebar", "setFav", "false") &
    JavaScriptHelper.call(Fav.htmlid, "favorites", "remove", "$(\"#" +"FavCh_" + widgetType + "_" + data.id.is + "\")")
  }
}