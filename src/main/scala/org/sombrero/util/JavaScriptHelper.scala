package org.sombrero.util

import net.liftweb.http.js._
import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.scala.util.Random 

object JavaScriptHelper {
	val emptyFunction = JsRaw("function(){}").cmd;
 
 	def onLoad(content: String): NodeSeq = {
		<head>
			<script type="text/javascript">
				{ Unparsed("""
						$(document).ready(function(){""" +
							content
						+ """});
				""")}  
			</script>
		</head>  
	}
 
	def createWidget(id:String, function:String, properties:List[(String, String)], content:NodeSeq): NodeSeq = {
		onLoad(initWidget(id, function, properties))++ <div id={id}></div>
    }
 
	def initWidget(id:String, function:String, properties:List[(String, String)]):String = {
			"""$("#""" + id + """").""" + function + "({" + 
				properties.slice(1).foldLeft ((properties(0)._1 + ": " + properties(0)._2))
                {(x, y) => x + ", " + y._1 + ": " + y._2} + 	
            "});" 
	}
 
 	def error(mes: String): JsExp = JsRaw("$().message(\"Hello world!\")")
 
 	def call(id:String, widgetType:String, option:String, value:String) = 
 		JsRaw("$('#" + id + "')." + widgetType + "('" + option + "', " + value + ");")
 	def css(id:String, attr: String): JsExp = 
 		JsRaw("$('#" + id + "').css('" + attr + "')")
 	def getOption(id:String, widgetType:String, option:String): JsExp = 
 		JsRaw("$('#" + id + "')." + widgetType + "('option', '" + option + "')")
  
   	def setOption(id:String, widgetType:String, option:String, value:String): JsExp = 
 		JsRaw("$('#" + id + "')." + widgetType + "('option', '" + option + "', " + value + "); " + "$('#" + id + "')." + widgetType + "('updateStatus')")
  
 	def callback(method: () => JsCmd) = "function(){" + SHtml.ajaxInvoke(method)._2 + ";}"
  
 	/**
 	 * @param id the id-attribute of the link with the href of the iframe
     */
   	def popup(id:String, title: String, content: NodeSeq): NodeSeq = onLoad(initWidget(id, "fancybox", Map(
		    		"width"				-> "\"75%\"",
		    		"height"			-> "\"75%\"",
		            "autoScale"     	-> "false",
		            "transitionIn"		-> "\"none\"",
		    		"transitionOut"		-> "\"none\"",
		    		"\"type\""			-> "\"iframe\"",
		    		"title"				-> ("\"" + title + "\"")
 	).toList))
}
