package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import org.sombrero.model
import _root_.scala.xml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

/**
 * This snippet renders the Tree View navigation Sidebar 
 * @author Gabriel Grill
 */
class TreeView{
  def render(xhtml : NodeSeq): NodeSeq = { JavaScriptHelper.onLoad(
		//creates widget with the properties in the list
		//to learn about the meaning of the properties see toserve/widget/js/ui.ipod.js 
    JavaScriptHelper.initWidget("TreeView", "ipod", List(
    		("top", "40"),
			("left", "5"),
			("initPath", "[" + getPath(Room.current) + "]")
		)))++ <div id="TreeView" class="hidden">
        	<ul>
            	<lift:RoomList >
            		<li><room:link />
            			<ul><room:children /></ul>
            		</li>
            	</lift:RoomList>
            </ul>
        </div>
    }
    
   //if a room has no children -> the parent room and his predecessor (and so on..) will be added recursively to the path
    //if a room has children -> the current room and his predecessor (and so on..) will be added recursively to the path
    def getPath(room: Box[model.Room]): String = {
      def rek(room: Box[model.Room]): String = {
        //adds room to the path and calls the recursive function rek
        //to do this to all parents until the root parent is reached
    	  room.map((r: model.Room) => ", '." + r.id.is + "'" + rek(r.parent.obj)) openOr ""
      }
      
   //if a room has no children -> the parent room 
    //if a room has children -> the current room 
      (if(room.map(_.children.isEmpty) openOr false) room.map(_.parent.obj) openOr Empty else room).map(
        //adds the room returned from the previous statement to the path and calls the recursive function rek
        //to do this to all parents until the root parent is reached
        (r: model.Room) => "'." + r.id.is + "'" + rek(r.parent.obj)) openOr ""
    }
}
