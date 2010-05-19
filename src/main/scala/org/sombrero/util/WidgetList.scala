package org.sombrero.util

import org.sombrero.model
import org.sombrero.widget._
import _root_.net.liftweb.http.SHtml
import org.sombrero.widget.knx._


object WidgetList {
  case class WidgetClass[MapperT <: model.WidgetData[MapperT]] (
      name : String, id : String, data : model.WidgetMetaData[MapperT], factory : (model.Widget, widget.WidgetPlace) => widget.Widget) {
    def widget(w : model.Widget) = factory(w, FavParent)
    def favwidget(w : model.Widget) = factory(w, FavChild)
    def admwidget(w : model.Widget) = factory(w, AdminSideBar)
  }

  /*object DummyWidget extends widget.Widget(null, null) {
    override def render = scala.xml.Text("")
  }*/
  
  val map = Map(
  "Lamp"        -> WidgetClass[model.KNXWidget] ("Lamp", "Lamp", model.KNXWidget, new Lamp(_,_)),
  "Temperature" -> WidgetClass[model.KNXWidget] ("Temperature", "Temperature", model.KNXWidget, new Temperature(_,_)),
  "SwitchOn"    -> WidgetClass[model.KNXWidget] ("SwitchOn", "SwitchOn", model.KNXWidget, new SwitchOn(_,_)),
  "SwitchOff"   -> WidgetClass[model.KNXWidget] ("SwitchOff", "SwitchOff", model.KNXWidget, new SwitchOff(_,_)),
  "Switch"      -> WidgetClass[model.KNXWidget] ("Switch", "Switch", model.KNXWidget, new Switch(_,_)),
  "Dimmer"      -> WidgetClass[model.KNXWidget] ("Dimmer", "Dimmer", model.KNXWidget, new Dimmer(_,_)),
  "Rollo"       -> WidgetClass[model.KNXWidget] ("Rollo", "Rollo", model.KNXWidget, new Rollo(_,_)),
  "Roomlink"    -> WidgetClass[model.RoomlinkWidget] ("Roomlink", "Roomlink", model.RoomlinkWidget, new RoomLink(_,_))
  )
  
  /*
  val map = Map(
  "Lamp"        -> WidgetClass[model.KNXWidget] ("Lamp", "Lamp", model.KNXWidget, new Lamp(_) with FavParent),
  "Temperature" -> WidgetClass[model.KNXWidget] ("Temperature", "Temperature", model.KNXWidget, new Temperature(_) with FavParent),
  "SwitchOn"    -> WidgetClass[model.KNXWidget] ("SwitchOn", "SwitchOn", model.KNXWidget, new SwitchOn(_) with FavParent),
  "SwitchOff"   -> WidgetClass[model.KNXWidget] ("SwitchOff", "SwitchOff", model.KNXWidget, new SwitchOff(_) with FavParent),
  "Switch"      -> WidgetClass[model.KNXWidget] ("Switch", "Switch", model.KNXWidget, new Switch(_) with FavParent),
  "Dimmer"      -> WidgetClass[model.KNXWidget] ("Dimmer", "Dimmer", model.KNXWidget, new Dimmer(_) with FavParent),
  "Rollo"       -> WidgetClass[model.KNXWidget] ("Rollo", "Rollo", model.KNXWidget, new Rollo(_) with FavParent),
  "Roomlink"    -> WidgetClass[model.RoomlinkWidget] ("Roomlink", "Roomlink", model.RoomlinkWidget, new RoomLink(_) with FavParent))
  */

 val default = map("Lamp")

}
