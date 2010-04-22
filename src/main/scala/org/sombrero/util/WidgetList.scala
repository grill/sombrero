package org.sombrero.util

import org.sombrero.model
import org.sombrero.widget
import _root_.net.liftweb.http.SHtml



object WidgetList {
  case class WidgetClass[T <: model.WidgetData[T]] (name : String, id : String, data : model.WidgetMetaData[T], factory : (model.Widget) => widget.Widget)

  object DummyWidget extends widget.Widget(null, null, null, null) {
    var properties : List[(String, String)] = Nil
    override def render = scala.xml.Text("")
  }
  
  val map = Map(
  "Lamp"        -> WidgetClass[model.KNXWidget] ("Lamp", "Lamp", model.KNXWidget, widget.knx.Lamp(_)),
  "Temperature" -> WidgetClass[model.KNXWidget] ("Temperature", "Temperature", model.KNXWidget, widget.knx.Temperature(_)),
  "SwitchOn"    -> WidgetClass[model.KNXWidget] ("SwitchOn", "SwitchOn", model.KNXWidget, widget.knx.SwitchOn(_)),
  "SwitchOff"   -> WidgetClass[model.KNXWidget] ("SwitchOff", "SwitchOff", model.KNXWidget, widget.knx.SwitchOff(_)),
  "Switch"      -> WidgetClass[model.KNXWidget] ("Switch", "Switch", model.KNXWidget, widget.knx.Switch(_)),
  "Dimmer"      -> WidgetClass[model.KNXWidget] ("Dimmer", "Dimmer", model.KNXWidget, widget.knx.Dimmer(_)),
  "Rollo"       -> WidgetClass[model.KNXWidget] ("Rollo", "Rollo", model.KNXWidget, widget.knx.Rollo(_)),
  "Roomlink"    -> WidgetClass[model.RoomlinkWidget] ("Roomlink", "Roomlink", model.RoomlinkWidget, _ => DummyWidget))
  
  
  val default = map("Lamp")
} 
   
