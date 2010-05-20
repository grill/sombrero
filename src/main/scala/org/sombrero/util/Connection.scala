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

object Connection {
  	var link: KNXNetworkLinkIP = null
	var knxComm: ProcessCommunicator = null
 
  	def createConnection(remoteHost:String) = {
  		Log.info("du")
  		link = new KNXNetworkLinkIP(remoteHost, TPSettings.TP1)
  		Log.info("du")
  		if(isConnected){   
  			Log.info("hi")
  			knxComm = new ProcessCommunicatorImpl(link)
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
 