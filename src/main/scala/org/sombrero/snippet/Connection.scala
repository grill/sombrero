package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.net.liftweb.http._
import S._
import SHtml._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.scala.util.Random
// Use logging facilities
import _root_.net.liftweb.util.Log
import java.net._
import net.liftweb.http.js._
import org.sombrero.util._
import bootstrap._
  
class Connection {
	def render(xhtml:NodeSeq) = {
		def create(): JsCmd = {
		  KNXRouter.getIP.map(ip => util.Connection.createConnection(ip))
		  JsRaw(";").cmd
		}
  
		def destroy():JsCmd = {
		  if(org.sombrero.util.Connection.isConnected) org.sombrero.util.Connection.destroyConnection
		  JsRaw(";").cmd
		}
	  
		bind("cn", xhtml,
				"create" 	-> SHtml.ajaxButton(Text("connect"), create _), 
				"destroy" 	-> SHtml.ajaxButton(Text("disconnect"), destroy _))
	}
}
