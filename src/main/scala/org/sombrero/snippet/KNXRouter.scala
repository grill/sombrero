//author: Alexander C. Steiner
package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model
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

//KNX router functionality, see also comet.Discovery
class KNXRouter {
  
  object redoSnippet extends RequestVar[Box[(NodeSeq) => NodeSeq]](Empty) 
    
  def set(xhtml : NodeSeq) : NodeSeq =
    redoSnippet.is.map(_(xhtml)) openOr {
    var r = model.KNXRouter.get
    
    def realrender(xhtml : NodeSeq) : NodeSeq =
    bind("knxrouter", xhtml,
         "ip" -> r.ip.toForm.openOr(Text("")),
         "set" -> submit("Set Router IP", onSubmit _))
    
    def onSubmit {
      r.validate match {
        case Nil => r.save; redoSnippet(Empty)
        case xs => S.error(xs); redoSnippet(Full(realrender _))
      }
    }
    
    realrender(xhtml)
  }
  
  def ip(ignore : NodeSeq) : NodeSeq = Text(model.KNXRouter.getIP openOr "None!")
}
