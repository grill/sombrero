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

object CometWidget {
  def render(r : model.Room) = <lift:comet type="RoomCometWidget" name={r.id.is.toString} />
  def render(u : model.User) = <lift:comet type="FavCometWidget" name={u.id.is.toString} />
  def renderAdmin() = <lift:comet type="AdminCometWidget" name="acw" />
  def renderNinja() = <lift:comet type="NinjaCometWidget" name={model.User.currentUser.open_!.id.is.toString + ":" + model.Room.current.open_!.id.toString} />
}

class NinjaCometWidget extends CometWidget {
  override def render = Text("")
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
        model.Widget.roomless.
        map(w => WidgetList.map(w.wclass.is).admwidget(w))
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

abstract class CometWidget extends CometActor {
  override def defaultPrefix = Full("cw")
  
  override def devMode = true
  
  var parent : List[widget.Widget] = Nil
  override def render = {
    //val w = parent.data.reload
    //parent = WidgetList.map(w.wclass.is).factory(w)
    NodeSeq.view(parent.flatMap(_.render))
  }
  
  override def lowPriority : PartialFunction[Any, Unit] = {
    case TestMessage(id, text) => {
      System.out.println("TestMessage recived for " + id + " " + text)
    }
    case DBMessage(_) => { 
      //reRender(true)
      partialUpdate(JsRaw("location.reload()").cmd)
    }
    /*case TitleMessage(_, s) => {
    	partialUpdate(parent.setTitle(s)) 
    }*/  
    case KNXMessage(id, value) => {
    	parent.filter(_.id == id).foreach(_ match {
    	  case p: StateWidget => { partialUpdate(p.setValue(value)) }
          case _ => {}
    	})
    }
  }
  
  def getWidgets(id : String) : List[widget.Widget]
  
  override def localSetup {
    //open_! : if it has no name, it's not ours
    parent = getWidgets(name.open_!)
    (Set() ++ parent.map(_.data.id.is)).foreach(wid => Distributor ! Subscribe(wid, this))
    super.localSetup
  }
  
  override def localShutdown {
    Distributor ! Unsubscribe(this)
    super.localShutdown
  }
}
