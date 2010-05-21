//author: Alexander C. Steiner
package org.sombrero.comet

import scala.actors._ 
import Actor._
import net.liftweb.http._
import net.liftweb.util._

//receives KNX updates and forwards them to the CometWidgets
object Distributor extends Actor{
    var map : Map[Long, List[CometActor]] = Map() withDefaultValue Nil
  Distributor.start

  def act = loop {
      react {
        case s : FavAddMessage => {
          (Set() ++ map.flatMap[CometActor](_._2)).foreach(_ ! s)
        }
        case s : SombreroMessage => {
          Log.info(s + " " + map(s.id))
          map(s.id).foreach(_ ! s)
        }
        case Subscribe(id, rec) => {
          Log.info("subscribe " + id + " " + rec)
          map = map(id) = rec :: map(id)
        }
        case Unsubscribe(rec) => {map = map.transform((id, l) => l - rec);}
        case PartialUnsubscribe(id, rec) => {map = map(id) = map(id) - rec;}
      }
    }
}
   
case class Subscribe(id : Long, rec : CometActor)
case class PartialUnsubscribe(id : Long, rec : CometActor)
case class Unsubscribe(rec : CometActor)
