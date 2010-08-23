package org.sombrero.util

import net.liftweb.http.js._
import _root_.net.liftweb.http._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.scala.util.Random
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import net.liftweb.json.JsonAST._


/**
 * This utility class contains methods to ease working with JavaScript code in Scala
 * @author Gabriel Grill
 */
object JavaScriptHelper {
  val emptyFunction = JsRaw("function(){}").cmd;
  
  //generates a script tag which will be through head merge rendered in the head tag
  def onLoad(content: String): NodeSeq = {
    <head>
      <script type="text/javascript">
        //content will be executed after the DOM has been fully loaded
        { Unparsed("$(document).ready(function(){" +
          content
        + "});")}  
      </script>
    </head>  
  }

 /**
  *  
  * @param id the id of the div tag which should get associated with the JQuery UI widget
  * @param function the name of the widget which should get associated to the selected div tag
  * @param properties a list of properties for the widget
  */
  def createWidget(id:String, function:String, properties: JObject): NodeSeq = {
    onLoad(initWidget(id, function, properties))++ <div id={id}></div>
  }

 /**
  * @param id the id of the div tag which should get associated with the JQuery UI widget
  * @param function the name of the widget which should get associated to the selected div tag
  * @param properties a list of properties for the widget
  */
  def initWidget(id:String, function:String, properties: JObject):String =
    "$('#" + id + "')." + function + "(" + pretty(render(properties)) + ");"
  
  //diplays a error message on the screen
  def error(mes: String): JsExp = JsRaw("$().message(\"Hello world!\")")

 /*
  * This two parameters are all used in the same way as described below. The other
  * parameters aren't explained, because they are self-explanatory.
  * @param id the id of the div tag which is associated with the JQuery UI widget
  * @param widgetType the name of the widget which is associated to the selected div tag
  */
  def call(id:String, widgetType:String, option:String, value:String) = 
    JsRaw("$('#" + id + "')." + widgetType + "('" + option + "', " + value + ");")
  def call(id:String, widgetType:String, option:String) = 
    JsRaw("$('#" + id + "')." + widgetType + "('" + option + "');")
  def css(id:String, attr: String): JsExp = 
    JsRaw("$('#" + id + "').css('" + attr + "')")
  def getOption(id:String, widgetType:String, option:String): JsExp = 
    JsRaw("$('#" + id + "')." + widgetType + "('option', '" + option + "')")
  
  def setOption(id:String, widgetType:String, option:String, value:String): JsExp = 
    JsRaw("$('#" + id + "')." + widgetType + "('option', '" + option + "', " + value + "); " +
    "$('#" + id + "')." + widgetType + "('updateStatus')")
  //generates a Ajax callback
  def callback(method: () => JsCmd) = SHtml.ajaxInvoke(method)._2.toString.replaceAll("&quot;", "'")
  
 /**
  * @param id the id-attribute of the link with the href of the iframe
  * @param title of the new dialog 
  */
/*  def popup(id:String, title: String, content: NodeSeq): NodeSeq = 
    onLoad(initWidget(id, "fancybox", Map(
    "width"				-> "\"75%\"",
    "height"			-> "\"75%\"",
		           "autoScale"     	-> "false",
		            "transitionIn"		-> "\"none\"",
		    		"transitionOut"		-> "\"none\"",
		    		"\"type\""			-> "\"iframe\"",
		    		"title"				-> ("\"" + title + "\"")
 	).toList)) ++ content
*/
 /**
  * @param id the id-attribute of the link with the href of the iframe
  * @param title of the new dialog 
  */
  def popupCmd(cls:String, title: String): JsCmd =
    JsRaw(initWidget(cls, "fancybox",
      ("width", "75%") ~
      ("height", "75%") ~
      ("autoScale", false) ~
      ("transitionIn", "none") ~
      ("transitionOut", "none") ~
      ("type", "iframe") ~
      ("title", title))).cmd
}
