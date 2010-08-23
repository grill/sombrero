package org.sombrero.util

import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import net.liftweb.http.js.jquery._
import _root_.net.liftweb.mapper._
import _root_.java.sql._

import org.sombrero.comet.SombreroKNXListener

import tuwien.auto.calimero._
import tuwien.auto.calimero.link._
import tuwien.auto.calimero.process._  
import tuwien.auto.calimero.link.medium._  

/**
 * This class contains a variaty of utility functions for connection management
 * @author Gabriel Grill
 */
@deprecated("Invalidated by SCalimero")
object Connection {
  var link: KNXNetworkLinkIP = null
  var knxComm: ProcessCommunicator = null
 
  //creates a connection
  def createConnection(remoteHost:String) = {
    link = new KNXNetworkLinkIP(remoteHost, TPSettings.TP1)
    if(isConnected){
      knxComm = new ProcessCommunicatorImpl(link)
      knxComm.setResponseTimeout(0)
      SombreroKNXListener.start
    }
  }

  def isConnected = link match {
    case null 	=> false
    case x		=> link.isOpen 
  }

  def destroyConnection = {
    knxComm.detach
    link.close
  }
}
