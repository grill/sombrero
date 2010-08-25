//author: Alexander C. Steiner
package org.sombrero.comet

import tuwien.auto.calimero.process._
import tuwien.auto.calimero.DetachEvent

import org.sombrero.model.KNXGroup
import org.sombrero.model.KNXWidget
import org.sombrero.util.Connection
import net.liftweb.mapper._
import tuwien.auto.scalimero.connection._
import scala.actors._

//forwards KNX updates to Distributor
//object SombreroKNXListener extends ProcessListener {
//  override def groupWrite(e : ProcessEvent) : Unit =
//    KNXGroup.getWidgets(e.getDestination.toString)
//    .foreach((w:KNXWidget) => Distributor ! KNXMessage(w.widget.is, e.getASDU))
//  
//  override def detached(e : DetachEvent) : Unit = {}
//  
//  def start {
//    Connection.knxComm.addProcessListener(this)
//  }
//}

object SombreroKNXListener extends Actor {
  def act {
    Network.default.subscribe(this)
    loop {
      react {
        case e : ProcessEvent => {
          KNXGroup.getWidgets(e.getDestination.toString)
          .foreach((w:KNXWidget) => Distributor ! KNXMessage(w.widget.is, e.getASDU))
        }
        case e : WriteEvent => {
          KNXGroup.getWidgets(e.destination.toString)
          .foreach((w:KNXWidget) => Distributor ! KNXWriteMessage(w.widget.is, e.value))
        }
      }
    }
  }
}
