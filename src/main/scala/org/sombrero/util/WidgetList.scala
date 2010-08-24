//author: Alexander C. Steiner
package org.sombrero.util

import org.sombrero.model
import org.sombrero.widget._
import org.sombrero.widget
import _root_.net.liftweb.http.SHtml
import org.sombrero.widget.knx._

//the widgetlist.
//stores information on the different widget types
//widget typesjust have to be added here to work
object WidgetList {
  case class WidgetClass[MapperT <: model.WidgetData[MapperT]] (
      name : String, id : String, data : model.WidgetMetaData[MapperT], factory : (model.Widget, org.sombrero.widget.WidgetPlace) => org.sombrero.widget.Widget) {
    def widget(w : model.Widget) = factory(w, FavParent)
    def favwidget(w : model.Widget) = factory(w, FavChild)
    def admwidget(w : model.Widget) = factory(w, AdminSideBar)
    def singlewidget(w : model.Widget) = factory(w, WidgetViewMode)
  }
  
  //would have been done as a package object member in Scala 2.8
  val map = Map(
  "Lamp"        -> WidgetClass[model.KNXWidget] ("Lamp", "Lamp", model.KNXWidget, new Lamp(_,_)),
  "Temperature" -> WidgetClass[model.KNXWidget] ("Temperature", "Temperature", model.KNXWidget, new Temperature(_,_)),
  "SwitchOn"    -> WidgetClass[model.KNXWidget] ("SwitchOn", "SwitchOn", model.KNXWidget, new SwitchOn(_,_)),
  "SwitchOff"   -> WidgetClass[model.KNXWidget] ("SwitchOff", "SwitchOff", model.KNXWidget, new SwitchOff(_,_)),
  "Switch"      -> WidgetClass[model.KNXWidget] ("Switch", "Switch", model.KNXWidget, new Switch(_,_)),
  "Dimmer"      -> WidgetClass[model.KNXWidget] ("Dimmer", "Dimmer", model.KNXWidget, new Dimmer(_,_)),
  "RollerBlind" -> WidgetClass[model.KNXWidget] ("RollerBlind", "RollerBlind", model.KNXWidget, new RollerBlind(_,_)),
  "Roomlink"    -> WidgetClass[model.RoomlinkWidget] ("Roomlink", "Roomlink", model.RoomlinkWidget, new RoomLink(_,_))
  )

 val default = map("Lamp")

}
