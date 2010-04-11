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
import scala.xml._

class CometWidget(parent: widget.Widget) extends CometActor {
  override def defaultPrefix = Full("cw")
      
  override def render = <lift:comet type="CometWidget" name={parent.id} />
  
  override def lowPriority : PartialFunction[Any, Unit] = {
    case TestMessage(id, text) => {
      System.out.println("TestMessage recived from " + id + " " + text)
    }
    case DBMessage(_) => { 
      
    }
    case TitleMessage(_, s) => {
    	partialUpdate(parent.setTitle(s)) 
    }  
    case KNXMessage(id, value) => {
    	parent match {
    	  case p: StateWidget => { partialUpdate(p.setValue(value)) }
          case _ => {}
    	}
    }
  }
}
