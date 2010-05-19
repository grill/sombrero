package org.sombrero.snippet

import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.sitemap._
import org.sombrero.model._

class Surround {
  def chooseRoom(xhtml: NodeSeq): NodeSeq = {
    def chooseTemplate() = {
      if (User.loggedIn_?)
    	  if (User.superUser_?) "default-admin" else "default-user" 
      else "default"
    }
    
    <lift:surround with={chooseTemplate} at="content">
    	{xhtml}
    </lift:surround>
  }
  
  def choose(xhtml: NodeSeq): NodeSeq = {
    def chooseTemplate() = {
      if (User.loggedIn_?)
    	  if (User.superUser_?) "default-admin-no-room" else "default-user" 
      else "default"
    }
    
    <lift:surround with={chooseTemplate} at="content">
    	{xhtml}
    </lift:surround>
  }
}
