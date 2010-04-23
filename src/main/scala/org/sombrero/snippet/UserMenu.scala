package org.sombrero.snippet

import _root_.net.liftweb.http._
import SHtml._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.sitemap._
import org.sombrero.model._
  
class UserMenu {
  def render(xhtml: NodeSeq): NodeSeq = {
	if (User.loggedIn_?) {
	  bind("menu", xhtml, 
	  "link1" -> {
	    <lift:UserMenu.build name="EditUser">Edit User</lift:UserMenu.build>},
	  "link2" -> {
<<<<<<< HEAD:src/main/scala/org/sombrero/snippet/UserMenu.scala
	    <lift:UserMenu.build name="ChangePassword">Change Password</lift:UserMenu.build>},
	  "link3" -> {
	  	<lift:UserMenu.build name="Logout">LogOut</lift:UserMenu.build>})
=======
	    <lift:UserMenu.build name="ChangePassword">Change Password</lift:UserMenu.build>})
>>>>>>> origin/master:src/main/scala/org/sombrero/snippet/UserMenu.scala
	} else {
	  	  bind("menu", xhtml, 
	  "link1" -> {
	    <lift:UserMenu.build name="Login">LogIn</lift:UserMenu.build>},
	  "link2" -> {
	    <lift:UserMenu.build name="CreateUser">Sign up</lift:UserMenu.build>})
	}
  }
  
  def build(text: NodeSeq): NodeSeq = {
    for (name <- S.attr("name").toList;  
        request <- S.request.toList;  
        loc <- request.location.toList;  
        item <- if (loc.name != name) SiteMap.buildLink(name, text) else text)
   yield item match {  
     case e: Elem => e % S.prefixedAttrsToMetaData("a")  
     case x => x  
   }  
  }
}
