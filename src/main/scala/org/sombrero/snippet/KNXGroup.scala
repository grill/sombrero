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

class KNXGroup {
  def list(xhtml : NodeSeq) : NodeSeq = {
    model.KNXGroup.findAll().flatMap(
      (g : model.KNXGroup) =>
      bind("knxgroup", xhtml,
           "name" -> g.name.is,
           "address" -> g.address.is,
           "delete" -> submit("Delete Group", g.delete_! _))
    )
  }
  
  object redoSnippet extends RequestVar[Box[(NodeSeq) => NodeSeq]](Empty) 
    
  def add(xhtml : NodeSeq) : NodeSeq =
    redoSnippet.is.map(_(xhtml)) openOr {
    var g = model.KNXGroup.create
    
    def realrender(xhtml : NodeSeq) : NodeSeq =
    bind("knxgroup", xhtml,
         "name" -> g.name.toForm.openOr(Text("")),
         "address" -> g.address.toForm.openOr(Text("")),
         "add" -> submit("Add Group", onSubmit _))
    
    def onSubmit {
      g.validate match {
        case Nil => g.save; redoSnippet(Empty)
        case xs => S.error(xs); redoSnippet(Full(realrender _))
      }
    }
    
    realrender(xhtml)
  }
}
