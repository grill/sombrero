package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.scala.xml._
import _root_.net.liftweb.util._

class TreeView{
  def render(xhtml : NodeSeq): NodeSeq = { JavaScriptHelper.onLoad(
    JavaScriptHelper.initWidget("TreeView", "ipod", List(
    		("top", "70"),
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
    
    def getPath(room: Box[model.Room]): String = {
      def rek(room: Box[model.Room]): String = {
    	  room.map((r: model.Room) => ", '." + r.id.is + "'" + rek(r.parent.obj)) openOr ""
      }
      (if(room.map(_.children.isEmpty) openOr false) room.map(_.parent.obj) openOr Empty else room).map(
        (r: model.Room) => "'." + r.id.is + "'" + rek(r.parent.obj)) openOr ""
    }
}
