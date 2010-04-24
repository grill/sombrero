package org.sombrero.util
 
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import SHtml._
import Helpers._
import _root_.net.liftweb.mapper._
import _root_.java.sql._
import _root_.scala.xml._
import org.sombrero.model._

object WidgetAdd {
  def render(where : Room)(ignore : NodeSeq) : NodeSeq = render(where, ignore, false)

  def render(where : Room, ignore : NodeSeq, inFrame : Boolean) : NodeSeq = {
    val w : Widget = Widget.create
    
    /*def realrender(ignore : NodeSeq) : NodeSeq = {
      w.toForm(Empty, realrender _, _.room(where).save) ++ w.dataForm(realrender _, _.save) ++
      submit("Save Widget", () => S.redirectTo("/room/" + where.id.is))
    }
    realrender(ignore)*/
    
    w.completeForm("Save Widget", (w, wd) => w.room(where), if(inFrame) "/closeframe" else "/room/" + where.id.is)
  }
  
  def render(ignore : NodeSeq) : NodeSeq = render(ignore, false)
  
  def render(ignore : NodeSeq, inFrame : Boolean) : NodeSeq = {
    val w : Widget = Widget.create
    
    /*def realrender(ignore : NodeSeq) : NodeSeq = {
      w.toForm(Empty, realrender _, _.save) ++ w.dataForm(realrender _, _.save) ++
      submit("Save Widget", () => S.redirectTo("/"))
    }
    realrender(ignore)*/
    
    w.completeForm("Save Widget", (w, wd) => null, if(inFrame) "/closeframe" else "/")
  }
}
