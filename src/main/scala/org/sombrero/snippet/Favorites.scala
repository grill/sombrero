package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.scala.xml._
import _root_.net.liftweb.util._
import org.sombrero.widget.knx._
import org.sombrero._
<<<<<<< HEAD:src/main/scala/org/sombrero/snippet/Favorites.scala
import org.sombrero.widget._
=======
>>>>>>> origin/master:src/main/scala/org/sombrero/snippet/Favorites.scala

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
<<<<<<< HEAD:src/main/scala/org/sombrero/snippet/Favorites.scala
	    	case w if(w.wclass.is == "Lamp") => new Lamp(w) with FavChild
	    	case w if(w.wclass.is == "Temperature") => new Temperature(w) with FavChild
	    	case w if(w.wclass.is == "SwitchOn") => new SwitchOn(w) with FavChild
	    	case w if(w.wclass.is == "SwitchOff") => new SwitchOff(w) with FavChild
	    	case w if(w.wclass.is == "Switch") => new Switch(w)  with FavChild
	    	case w if(w.wclass.is == "Dimmer") => new Dimmer(w) with FavChild
	    	case w if(w.wclass.is == "Rollo") => new Rollo(w) with FavChild
=======
	    	case w if(w.wclass.is == "Lamp") => LampFavCopy(w)
	    	case w if(w.wclass.is == "Temperature") => TemperatureFavCopy(w)
	    	case w if(w.wclass.is == "SwitchOn") => SwitchOnFavCopy(w)
	    	case w if(w.wclass.is == "SwitchOff") => SwitchOffFavCopy(w)
	    	case w if(w.wclass.is == "Switch") => SwitchFavCopy(w) 
	    	case w if(w.wclass.is == "Dimmer") => DimmerFavCopy(w)
	    	case w if(w.wclass.is == "Rollo") => RolloFavCopy(w)
>>>>>>> origin/master:src/main/scala/org/sombrero/snippet/Favorites.scala
	    	case _ => null
            //else /* if (w.wclass.is == "Temperature")*/ new Temperature(w)
	    })
       .filter(_ != null)
       .foldLeft[List[Node]](Nil)((l, n : widget.Widget) => l ::: n.render.toList) : NodeSeq 
	  else
		  Nil
	}
}
