//author: Alexander C. Steiner
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
  
//restricts parts of the page to superusers
class SuperUser {
	
	def has(xhtml:NodeSeq) = {
	  if(org.sombrero.model.User.superUser_?)
      xhtml
    else
	    Text("")
	}
}
