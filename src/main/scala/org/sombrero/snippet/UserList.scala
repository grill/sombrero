package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._
import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Log
import java.net._;
import _root_.net.liftweb.mapper._

class UserList {

  def render(xhtml : NodeSeq) : NodeSeq = {
    def entry(user : User) : NodeSeq = {
      var newPasswd : List[String] = Nil
      
      def testAndSet {
        user.password.setFromAny(newPasswd)
        user.validate match {
          case Nil => user.save; S.notice(user.email.is + "'s password set")
          case xs => S.error(xs)
        }
      }
      
      def testAndDelete {
        if(user.superUser.is && User.count(By(User.superUser, true)) == 1)
          S.error("you can't delete the last admin")
        else
          user.delete_!
      }
    
      bind("userlist", xhtml, "entries" -> ((insidexhtml) =>
      bind("user", insidexhtml,
           "link" -> link("/useredit/" + user.id.is, () => Empty, Text(user.email.is)),
           "newPw" -> password_*("", LFuncHolder(newPasswd = _)),
           "submit" -> submit("Set Password", testAndSet _),
           "delete" -> submit("Delete User", testAndDelete _)
         )))
    }
    
    User.findAll().flatMap(entry(_))
  }
}