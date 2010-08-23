package org.sombrero.comet

import org.scalimero.connection.Network
import net.liftweb.actor.LiftActor
import tuwien.auto.calimero.link.medium._

object SombreroNetwork {
  
  def apply(router : String, medium : KNXMediumSettings = Network.defaultMedium) = {
    Network.default = new SombreroNetwork(router, medium)
    Network.default
  }
  
  def open = Network.default.open
  def close = Network.default.close
  
  def open_? = (Network.default != null) && (Network.default.opened)
}

class SombreroNetwork(router : String, medium : KNXMediumSettings = Network.defaultMedium) extends Network(router, medium) {
  override val act = new LiftActor {override def messageHandler = actorBody}
}
