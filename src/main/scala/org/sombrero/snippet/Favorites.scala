package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.scala.xml._
import _root_.net.liftweb.util._
import org.sombrero.widget.knx._
import org.sombrero._
import org.sombrero.widget._

class Favorites {

	def render(xhtml: NodeSeq): NodeSeq = {
		<div id={Fav.htmlid}> {getWidgets} </div> ++ JavaScriptHelper.onLoad(
		JavaScriptHelper.initWidget(Fav.htmlid, "favorites", List(
			("left", "250"),
			("top", "50"),
			("amount_widgets", "5"),
			("width", "162"),
			("height", "195")
		)))     
	}
 
	def getWidgets(): NodeSeq = {
	  val l = Fav.get
     
	  if(l != Nil)
	    l.map((w : model.Widget) => w match {
	    	case w if(w.wclass.is == "Lamp") => new Lamp(w) with FavChild
	    	case w if(w.wclass.is == "Temperature") => new Temperature(w) with FavChild
	    	case w if(w.wclass.is == "SwitchOn") => new SwitchOn(w) with FavChild
	    	case w if(w.wclass.is == "SwitchOff") => new SwitchOff(w) with FavChild
	    	case w if(w.wclass.is == "Switch") => new Switch(w)  with FavChild
	    	case w if(w.wclass.is == "Dimmer") => new Dimmer(w) with FavChild
	    	case w if(w.wclass.is == "Rollo") => new Rollo(w) with FavChild
	    	case _ => null
            //else /* if (w.wclass.is == "Temperature")*/ new Temperature(w)
	    })
       .filter(_ != null)
       .foldLeft[List[Node]](Nil)((l, n : widget.Widget) => l ::: n.render.toList) : NodeSeq 
	  else
		  Nil
	}
}
