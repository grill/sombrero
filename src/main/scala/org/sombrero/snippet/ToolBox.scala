package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.model
import org.sombrero.widget
import _root_.scala.xml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import org.sombrero.widget.knx._
import org.sombrero.widget._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

/**
 * This snippet renders the Admin's toolbox in the SideBar
 * and works exactly like Favorites. For further need of information
 * look into Favorites.scala.
 * 
 * @author Gabriel Grill
 */
class ToolBox {
  def render(xhtml: NodeSeq): NodeSeq = {
    if(Room.current != Empty){
      <div id={ToolBox.id}> {getWidgets} </div> ++ JavaScriptHelper.onLoad(
        JavaScriptHelper.initWidget(ToolBox.id, "widgetcontainer",
          ("left", 50) ~ ("top", 20) ~ ("amountWidgets", 3) ~
          ("width", 195) ~ ("height", 195) ~
          ("vertical", true) ~ ("isAdminMode", true)
      ))
  }else{
      Nil
    }
  } 
    
  def getWidgets(): NodeSeq = {
    //gets the List of widgets in the toolbox
    val l = model.Widget.roomless 

    if(l != Nil)
      l.map((w : model.Widget) => WidgetList.map(w.wclass.is).admwidget(w)).
      foldLeft[List[Node]](Nil)((l, n : widget.Widget) => l ::: n.render.toList) : NodeSeq
    else
      Nil
  }
}

object ToolBox { 
  val id = "ToolBoxContainer"
}
