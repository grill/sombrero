package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.scala.xml._
import _root_.net.liftweb.util._
import org.sombrero.widget.knx._
import org.sombrero.widget._

class ToolBox {

	def render(xhtml: NodeSeq): NodeSeq = {
	  if(Room.current != Empty){
		  <div id={ToolBox.id}> {getWidgets} </div> ++ JavaScriptHelper.onLoad(
				JavaScriptHelper.initWidget(ToolBox.id, "favorites", List(
				("left", "50"),
				("top", "20"),
				("amount_widgets", "3"),
				("width", "195"),
				("height", "195"),
				("vertical", "true"),
				("admin_mode", "true")
			)))
		}else{
			Nil
		}
	} 
    
	def getWidgets(): NodeSeq = {
	  val l = model.Widget.roomless 
     
	  if(l != Nil)
	    l.map((w : model.Widget) => WidgetList.map(w.wclass.is).admwidget(w)).
      foldLeft[List[Node]](Nil)((l, n : widget.Widget) => l ::: n.render.toList) : NodeSeq
//	    l.map((w : model.Widget) => w match {
//	    	case w if(w.wclass.is == "Lamp") => new Lamp(w) with AdminSideBar
//	    	case w if(w.wclass.is == "Temperature") => new Temperature(w) with AdminSideBar
//	    	case w if(w.wclass.is == "SwitchOn") => new SwitchOn(w) with AdminSideBar
//	    	case w if(w.wclass.is == "SwitchOff") => new SwitchOff(w) with AdminSideBar
//	    	case w if(w.wclass.is == "Switch") => new Switch(w)  with AdminSideBar
//	    	case w if(w.wclass.is == "Dimmer") => new Dimmer(w) with AdminSideBar
//	    	case w if(w.wclass.is == "Rollo") => new Rollo(w) with AdminSideBar
//	    	case w if(w.wclass.is == "Roomlink") => new RoomLink(w) with AdminSideBar
//	    	case _ => null
//            //else /* if (w.wclass.is == "Temperature")*/ new Temperature(w)
//	    })
//       .filter(_ != null)
//       .foldLeft[List[Node]](Nil)((l, n : widget.Widget) => l ::: n.render.toList) : NodeSeq 
	  else
		  Nil
	}
}

object ToolBox { 
  val id = "ToolBoxContainer"
}
