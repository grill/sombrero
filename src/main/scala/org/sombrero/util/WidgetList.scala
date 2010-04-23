package org.sombrero.util

import org.sombrero.model
import org.sombrero.widget._
import _root_.net.liftweb.http.SHtml
import org.sombrero.widget.knx._


object WidgetList {
  case class WidgetClass[T <: model.WidgetData[T]] (name : String, id : String, data : model.WidgetMetaData[T], factory : (model.Widget) => widget.Widget)

  object DummyWidget extends widget.Widget(null, null) {
    override def render = scala.xml.Text("")
  }  
  
  val map = Map(
  "Lamp"        -> WidgetClass[model.KNXWidget] ("Lamp", "Lamp", model.KNXWidget, new Lamp(_) with FavParent),
  "Temperature" -> WidgetClass[model.KNXWidget] ("Temperature", "Temperature", model.KNXWidget, new Temperature(_) with FavParent),
  "SwitchOn"    -> WidgetClass[model.KNXWidget] ("SwitchOn", "SwitchOn", model.KNXWidget, new SwitchOn(_) with FavParent),
  "SwitchOff"   -> WidgetClass[model.KNXWidget] ("SwitchOff", "SwitchOff", model.KNXWidget, new SwitchOff(_) with FavParent),
  "Switch"      -> WidgetClass[model.KNXWidget] ("Switch", "Switch", model.KNXWidget, new Switch(_) with FavParent),
  "Dimmer"      -> WidgetClass[model.KNXWidget] ("Dimmer", "Dimmer", model.KNXWidget, new Dimmer(_) with FavParent),
  "Rollo"       -> WidgetClass[model.KNXWidget] ("Rollo", "Rollo", model.KNXWidget, new Rollo(_) with FavParent),
  "Roomlink"    -> WidgetClass[model.RoomlinkWidget] ("Roomlink", "Roomlink", model.RoomlinkWidget, new RoomLink(_) with FavParent))
} 
   