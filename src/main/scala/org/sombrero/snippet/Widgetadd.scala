//author: Alexander C. Steiner
package org.sombrero.snippet

import scala.xml._
import org.sombrero.model
import net.liftweb.util._
import _root_.net.liftweb.common._
import net.liftweb.http.RequestVar
import net.liftweb.http.S
import org.sombrero.util.Log

//widget adding forms
class Widgetadd {
  def render(ignore : NodeSeq) : NodeSeq = render(ignore, false)
  
  def render(ignore : NodeSeq, inFrame : Boolean) : NodeSeq = Widgetadd.newData.is match {
    case Full(wd) =>
      model.Widget.create.completeForm(
        wd,
        "Save Widget",
        (w, wd : model.KNXWidget) => w.room(model.Room.current),
        if(inFrame) "/closeframe"
          else model.Room.current.map("/room/" + _.id.is) openOr "/"
      )
    case _ =>
      model.Widget.create.completeForm(
        "Save Widget",
        (w, wd) => w.room(model.Room.current),
        if(inFrame) "/closeframe"
          else model.Room.current.map("/room/" + _.id.is) openOr "/"
      )
  }
  
  def form(ignore : NodeSeq) : NodeSeq = {Log.info("widgetadd here: " + Widgetadd.newData.is); render(ignore, true)}
}

object Widgetadd {
  object newData extends RequestVar[Box[model.KNXWidget]](S.param("groupAddress").map((s) => model.KNXWidget.create.groupAddress(s)))
}
