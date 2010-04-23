package org.sombrero.comet

import scala.actors._ 
import Actor._
<<<<<<< HEAD:src/main/scala/org/sombrero/comet/Distributor.scala
import net.liftweb.http._
=======
import net.liftweb.http._
import net.liftweb.util._
>>>>>>> origin/master:src/main/scala/org/sombrero/comet/Distributor.scala

object Distributor extends Actor{
    var map : Map[Long, List[CometActor]] = Map() withDefaultValue Nil
  Distributor.start

  val act = loop {
      receive {
<<<<<<< HEAD:src/main/scala/org/sombrero/comet/Distributor.scala
        case s : SombreroMessage => {map(s.id).foreach(_ ! s)}
=======
        case s : SombreroMessage => {
          Log.info(s + " " + map(s.id))
          map(s.id).foreach(_ ! s)
        }
>>>>>>> origin/master:src/main/scala/org/sombrero/comet/Distributor.scala
        case Subscribe(id, rec) => {map = map(id) = rec :: map(id)}
        case Unsucribe(rec) => {map = map.transform((id, l) => l - rec); reply()}
      }
    }
}
   
case class Subscribe(id : Long, rec : CometActor)
<<<<<<< HEAD:src/main/scala/org/sombrero/comet/Distributor.scala
case class Unsucribe(rec : CometActor)
=======
case class Unsucribe(rec : CometActor)
>>>>>>> origin/master:src/main/scala/org/sombrero/comet/Distributor.scala
