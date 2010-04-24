package org.sombrero.snippet

import scala.xml._
import org.sombrero.model
import org.sombrero.util.WidgetAdd
import net.liftweb.util._
import net.liftweb.http.RequestVar

class Widgetadd {
  def render(ignore : NodeSeq) : NodeSeq = render(ignore, false)
  /*
  model.Room.current match {
    case Full(room) => WidgetAdd.render(room)(ignore)
    case _ =>  WidgetAdd.render(ignore)
  }
  */
  
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
  
  def form(ignore : NodeSeq) : NodeSeq = render(ignore, true)
  /*
  model.Room.current match {
    case Full(room) => WidgetAdd.render(room, ignore, true)
    case _ =>  WidgetAdd.render(ignore, true)
  }
  */
}

object Widgetadd {
  object newData extends RequestVar[Box[model.KNXWidget]](Empty)
}
