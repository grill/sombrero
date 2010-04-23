package org.sombrero.util

import org.sombrero.model
<<<<<<< HEAD:src/main/scala/org/sombrero/util/WidgetList.scala
import org.sombrero.widget._
import _root_.net.liftweb.http.SHtml
import org.sombrero.widget.knx._
=======
import org.sombrero.widget
import _root_.net.liftweb.http.SHtml

>>>>>>> origin/master:src/main/scala/org/sombrero/util/WidgetList.scala


object WidgetList {
  case class WidgetClass[T <: model.WidgetData[T]] (name : String, id : String, data : model.WidgetMetaData[T], factory : (model.Widget) => widget.Widget)

<<<<<<< HEAD:src/main/scala/org/sombrero/util/WidgetList.scala
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
   
=======
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
   
>>>>>>> origin/master:src/main/scala/org/sombrero/util/WidgetList.scala
