package org.sombrero.snippet

import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Log
import java.net._;
import _root_.net.liftweb.mapper._

class WidgetMaster extends StatefulSnippet{
	var name = "";
	var dispatch : DispatchIt = {
		case "challenge" => firstPage _
	}
 
	def processName(nm: String) = {
	  name = nm
	  println(nm)
	}
 
	def firstPage(xhtml : NodeSeq) : NodeSeq = {
		bind("form", xhtml,
			"question" -> Text("What is your name? " + name),
			"answer" -> SHtml.text(name, processName))
	}
}
 