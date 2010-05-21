//author: Alexander C. Steiner
package org.sombrero.comet

import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import net.liftweb.http.js.jquery._
import _root_.net.liftweb.mapper._
import _root_.java.sql._
import scala.actors.Actor
import scala.actors.Actor._
import org.sombrero.widget._
import org.sombrero.model
import org.sombrero.util.WidgetList
import scala.xml._
import net.liftweb.http.js.JE.JsRaw

//the CometWidget handles updates for all widgets on the page
object CometWidget {
  def render(r : model.Room) = <lift:comet type="RoomCometWidget" name={r.id.is.toString} />
  def render(u : model.User) = <lift:comet type="FavCometWidget" name={u.id.is.toString} />
  def renderAdmin() = <lift:comet type="AdminCometWidget" name="acw" />
  def renderNinja() = <lift:comet type="NinjaCometWidget" name={model.User.currentUser.open_!.id.is.toString + ":" + model.Room.current.open_!.id.toString} />
}

//the implementation in use, it is stealthy (renders nothing) and updates everything
class NinjaCometWidget extends CometWidget {
  override def render = {
    Log.info(toString + ": render")
    Text("")
  }
  def getWidgets(id : String) = {id.split(":") match {
    case a if a.length == 2 =>
      val uid = a(0); val rid = a(1)
      Log.info("ninja: uid: " + uid + " rid: " + rid)
      (
        model.Widget.findAll(By(model.Widget.room, rid.toLong)).
        map(w => WidgetList.map(w.wclass.is).widget(w))
      ) ::: (
        model.Fav.findAll(By(model.Fav.user, uid.toLong)).
        map(_.widget.obj).filter(_ != Empty).map(_.open_!).
        map(w => WidgetList.map(w.wclass.is).favwidget(w))
      ) ::: (
        if(model.User.superUser_?) (
        model.Widget.roomless.
        map(w => WidgetList.map(w.wclass.is).admwidget(w))
        ) else Nil
      )
    case _ => Nil
  }}
}

class RoomCometWidget extends CometWidget {
  def getWidgets(id : String) = model.Widget.findAll(By(model.Widget.room, id.toLong)).
      map(w => WidgetList.map(w.wclass.is).widget(w))
}

class FavCometWidget extends CometWidget {
  def getWidgets(id : String) = model.Fav.findAll(By(model.Fav.user, id.toLong)).
    map(_.widget.obj).filter(_ != Empty).map(_.open_!).
    map(w => WidgetList.map(w.wclass.is).widget(w))
}

class AdminCometWidget extends CometWidget {
  def getWidgets(id : String) = model.Widget.roomless.
      map(w => WidgetList.map(w.wclass.is).widget(w))
}

//base class for different implementations
abstract class CometWidget extends CometActor {
  override def defaultPrefix = Full("cw")
  
  var parent : List[widget.Widget] = Nil
  override def render = {
    NodeSeq.view(parent.flatMap(_.render))
  }
  
  override def lowPriority : PartialFunction[Any, Unit] = {
    case TestMessage(id, text) => {
      Log.info("TestMessage recived for " + id + " " + text)
    }
    case DBMessage(_) => { 
      partialUpdate(JsRaw("location.reload()").cmd)
    }
    case KNXMessage(id, value) => {
    	Log.info(parent.filter(_.data.id.is == id))
    	parent.filter(_.data.id.is == id).foreach(_ match {
    	  case p: StateWidget => {Log.info(p); partialUpdate(p.setValue(value)) }
          case _ => {}
    	})
    }
    
    case FavAddMessage(id) => {
      Log.info("buh")
      if(! parent.exists(w => w.data.id.is == id && w.wp == FavChild && model.Fav.isFav(w.data))) {
        val l = 
        model.Fav.findAll(By(model.Fav.user, model.User.currentUser.open_!.id)).
        map(_.widget.obj).filter(_.map(_.id.is == id) openOr false).map(_.open_!).
        map(w => WidgetList.map(w.wclass.is).favwidget(w))
        Log.info("add:" + l.toString);
        l.filter(w => ! parent.exists(_.data.id.is == w.data.id.is)).
          foreach(w => Distributor ! Subscribe(w.data.id.is, this))
        l.foreach{w => 
          val cmd = w.addFavCmd
          Log.info(cmd)
          partialUpdate(cmd)
        }
        parent = l ::: parent
      }
    }
    
    case FavRemMessage(id) => {
      val l = parent.filter(w => w.data.id.is == id && w.wp == FavChild)
      Log.info(parent.map(_.data.id.is).toString);
      Log.info("remove:" + l.toString);
      l.filter(w => ! parent.exists(_.data.id.is == w.data.id.is)).
        foreach(w => Distributor ! PartialUnsubscribe(w.data.id.is, this))
      l.foreach(w => partialUpdate(w.removeFavCmd))
      parent = parent diff l
    }
    
    case something => Log.info(something.toString)
  }
  
  def getWidgets(id : String) : List[widget.Widget]
  
  override def localSetup {
    //open_! : if it has no name, it's not ours
    Log.info(toString + ": local setup")
    parent = getWidgets(name.open_!)
    (Set() ++ parent.map(_.data.id.is)).foreach(wid => Distributor ! Subscribe(wid, this))
    super.localSetup
  }
  
  override def localShutdown {
    Distributor ! Unsubscribe(this)
    super.localShutdown
  }
}
