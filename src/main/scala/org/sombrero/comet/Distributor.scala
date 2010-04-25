package org.sombrero.comet

import scala.actors._ 
import Actor._
import net.liftweb.http._
import net.liftweb.util._

object Distributor extends Actor{
    var map : Map[Long, List[CometActor]] = Map() withDefaultValue Nil
  Distributor.start

  def act = loop {
      react {
        case s : SombreroMessage => {
          Log.info(s + " " + map(s.id))
          map(s.id).foreach(_ ! s)
        }
        case Subscribe(id, rec) => {
          Log.info("subscribe " + id + " " + rec)
          map = map(id) = rec :: map(id)
        }
        case Unsubscribe(rec) => {map = map.transform((id, l) => l - rec); reply()}
      }
    }
}
   
case class Subscribe(id : Long, rec : CometActor)
case class Unsubscribe(rec : CometActor)
