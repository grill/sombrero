package org.sombrero.comet

import org.scalimero.connection.Network
import net.liftweb.actor.LiftActor
import tuwien.auto.calimero.link.medium._

class SombreroNetwork(router : String, medium : KNXMediumSettings = Network.defaultMedium) extends Network(router, medium) {
  override val act = new LiftActor {override def messageHandler = actorBody}
}
