package org.sombrero.comet

import scala.actors._ 
import Actor._
import net.liftweb.http._
import net.liftweb.util._

object Distributor extends Actor{
    var map : Map[Long, List[CometActor]] = Map() withDefaultValue Nil
  Distributor.start

  val act = loop {
      receive {
        case s : SombreroMessage => {
          Log.info(s + " " + map(s.id))
          map(s.id).foreach(_ ! s)
        }
        case Subscribe(id, rec) => {map = map(id) = rec :: map(id)}
        case Unsucribe(rec) => {map = map.transform((id, l) => l - rec); reply()}
      }
    }
}
   
case class Subscribe(id : Long, rec : CometActor)
case class Unsucribe(rec : CometActor)
