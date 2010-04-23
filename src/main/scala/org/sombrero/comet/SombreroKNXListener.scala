package org.sombrero.comet

import tuwien.auto.calimero.process._
import tuwien.auto.calimero.DetachEvent
<<<<<<< HEAD:src/main/scala/org/sombrero/comet/SombreroKNXListener.scala
=======
import org.sombrero.model.KNXGroup
>>>>>>> origin/master:src/main/scala/org/sombrero/comet/SombreroKNXListener.scala
import org.sombrero.model.KNXWidget
import org.sombrero.util.Connection
import net.liftweb.mapper._


object SombreroKNXListener extends ProcessListener {
  override def groupWrite(e : ProcessEvent) : Unit =
<<<<<<< HEAD:src/main/scala/org/sombrero/comet/SombreroKNXListener.scala
    KNXWidget.findAll(By(KNXWidget.groupAddress, e.getDestination.toString))
=======
    //KNXWidget.findAll(By(KNXWidget.groupAddress, e.getDestination.toString))
    KNXGroup.getWidgets(e.getDestination.toString)
>>>>>>> origin/master:src/main/scala/org/sombrero/comet/SombreroKNXListener.scala
    .foreach((w:KNXWidget) => Distributor ! KNXMessage(w.widget.is, e.getASDU))
  
  override def detached(e : DetachEvent) : Unit = {}
              
  def start {
    Connection.knxComm.addProcessListener(this)
  }
}
