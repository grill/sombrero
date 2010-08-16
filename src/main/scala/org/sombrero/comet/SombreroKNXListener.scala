//author: Alexander C. Steiner
package org.sombrero.comet

import tuwien.auto.calimero.process._
import tuwien.auto.calimero.DetachEvent

import org.sombrero.model.KNXGroup
import org.sombrero.model.KNXWidget
import org.sombrero.util.Connection
import net.liftweb.mapper._

//forwards KNX updates to Distributor
object SombreroKNXListener extends ProcessListener {
  override def groupWrite(e : ProcessEvent) : Unit =
    KNXGroup.getWidgets(e.getDestination.toString)
    .foreach((w:KNXWidget) => Distributor ! KNXMessage(w.widget.is, e.getASDU))
  
  override def detached(e : DetachEvent) : Unit = {}
              
  def start {
    Connection.knxComm.addProcessListener(this)
  }
}
