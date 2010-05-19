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
  def render(r : model.Room) = <lift:comet type="CometWidget" name={r.id.is.toString} />
}

class CometWidget extends CometActor {
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
  
  override def localSetup {
    //open_! : if it has no name, it's not ours
    parent = model.Widget.findAll(By(model.Widget.room, name.open_!.toLong)).
      map(w => WidgetList.map(w.wclass.is).factory(w))
    parent.foreach(w => Distributor ! Subscribe(w.data.id.is, this))
    super.localSetup
  }
  
  override def localShutdown {
    Distributor ! Unsubscribe(this)
    super.localShutdown
  }
}
