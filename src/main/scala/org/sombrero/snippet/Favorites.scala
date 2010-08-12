package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.scala.xml._
import _root_.net.liftweb.util._
import org.sombrero.widget.knx._
import org.sombrero._
import org.sombrero.widget._

/**
 * This snippet renders the favorits Sidebar
 * @author Gabriel Grill
 */
class Favorites {

	def render(xhtml: NodeSeq): NodeSeq = {
	    //the following div tag will be promoted to a favorites widget
		<div id={Fav.htmlid}> {getWidgets} </div> ++ JavaScriptHelper.onLoad(
		//creates widget with the properties in the list
		//to learn about the meaning of the properties see toserve/widget/js/ui.favorite.js 
		JavaScriptHelper.initWidget(Fav.htmlid, "favorites", List(
			("left", "250"),
			("top", "50"),
			("amount_widgets", "5"),
			("width", "162"),
			("height", "195")
		)))     
	}
 
	//returns all widgets that are placed in the favorites Sidebar
	def getWidgets(): NodeSeq = {
	  val l = Fav.get
     
	  if(l != Nil)
		//iterates through the list of model.Widget objects a and new List of widget.Widgets with FavChild as the WidgetPlace Parameter
	    l.map((w : model.Widget) => WidgetList.map(w.wclass.is).favwidget(w)).
        //iterates through the list and builds NodeSeq, containg all div tags and widget initialization script tags
        foldLeft[List[Node]](Nil)((l, n : widget.Widget) => l ::: n.render.toList) : NodeSeq
	  else
		  Nil
	}
}
