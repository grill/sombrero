package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.scala.xml._
import _root_.net.liftweb.util._
import org.sombrero.widget.knx._ 

class ToolBox {

	def render(xhtml: NodeSeq): NodeSeq = {
		<div id={ToolBox.id}> {getWidgets} </div> ++ JavaScriptHelper.onLoad(
		JavaScriptHelper.initWidget(ToolBox.id, "favorites", List(
			("left", "50"),
			("top", "430"),
			("amount_widgets", "1"),
			("width", "195"),
			("height", "195"),
			("vertical", "true"),
			("admin_mode", "true")
		)))
	} 
    
	def getWidgets(): NodeSeq = {
	  val l = model.Widget.roomless 
     
	  if(l != Nil)
	    l.map((w : model.Widget) => w match {
	    	case w if(w.wclass.is == "Lamp") => LampAdmCopy(w)
	    	case w if(w.wclass.is == "Temperature") => TemperatureAdmCopy(w)
	    	case w if(w.wclass.is == "SwitchOn") => SwitchOnAdmCopy(w)
	    	case w if(w.wclass.is == "SwitchOff") => SwitchOffAdmCopy(w)
	    	case w if(w.wclass.is == "Switch") => SwitchAdmCopy(w) 
	    	case w if(w.wclass.is == "Dimmer") => DimmerAdmCopy(w)
	    	case w if(w.wclass.is == "Rollo") => RolloAdmCopy(w)
	    	case _ => null
            //else /* if (w.wclass.is == "Temperature")*/ new Temperature(w)
	    })
       .filter(_ != null)
       .foldLeft[List[Node]](Nil)((l, n : widget.Widget) => l ::: n.render.toList) : NodeSeq 
	  else
		  Nil
	}
}

object ToolBox { 
  val id = "ToolBoxContainer"
}
