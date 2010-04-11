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
  def render(where : Room)(ignore : NodeSeq) : NodeSeq = {
    val w : Widget = Widget.create       
    /*
    val knx : KNXWidget = KNXWidget.create
    val rl : RoomlinkWidget = RoomlinkWidget.create
    var wok = false
    var knxok = false
    var rlok = false
    
    def doSubmit {
      w.wclass.is match {
        case s : String if (s equals "Temperature") || (s equals "Lamp") =>
            if(wok && knxok) {
              knx.widget(w.room(where).saveMe).save
              S.redirectTo("/room/" + where.id.is)
            }
        case s : String if s equals "Room" =>
            if(wok && rlok) {
              rl.widget(w.room(where).saveMe).save
              S.redirectTo("/room/" + where.id.is)
            }
        case _ => ;
      }
    }
    
    def realrender(ignore : NodeSeq) : NodeSeq =
    {
      wok = false; knxok = false; rlok = false
      w.toForm(Empty, realrender _, (w) => {Log.info("widget submit"); wok = true}) ++
      knx.toForm(Empty, realrender _,(w) => {Log.info("knx submit"); knxok = true}) ++
      rl.toForm(Empty, realrender _, (w) => {Log.info("roomlink submit"); rlok = true}) ++
      submit("Add Widget", doSubmit _)
    }
    
    realrender(ignore)
    */
    
    def realrender(ignore : NodeSeq) : NodeSeq = {
      w.toForm(Empty, realrender _, _.room(where).save) ++ w.dataForm(realrender _, _.save) ++
      submit("Save Widget", () => S.redirectTo("/room/" + where.id.is))
    }
    realrender(ignore)
  }
}
