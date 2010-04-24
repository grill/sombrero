package org.sombrero.snippet

import scala.xml._
import org.sombrero.model
import org.sombrero.util.WidgetAdd
import net.liftweb.util._

class Widgetadd {
  def render(ignore : NodeSeq) : NodeSeq = model.Room.current match {
    case Full(room) => WidgetAdd.render(room)(ignore)
    case _ =>  WidgetAdd.render(ignore)
  }
}
