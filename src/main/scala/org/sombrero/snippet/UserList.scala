//author: Alexander C. Steiner
package org.sombrero.snippet

import org.sombrero.util._
import org.sombrero.model._

import _root_.net.liftweb.http._
import S._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import Helpers._
import _root_.scala.xml._
import _root_.net.liftweb.http.js.{JE,JsCmd,JsCmds}
import JsCmds._ // For implicits
import JE.{JsRaw,Str}
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Log
import java.net._;
import _root_.net.liftweb.mapper._

//for userlist.html
class UserList {

  def render(xhtml : NodeSeq) : NodeSeq = {
    def entry(user : User, insidexhtml : NodeSeq) : NodeSeq = {
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
    
      bind("user", insidexhtml,
           "link" -> link("/useredit/" + user.id.is, () => Empty, Text(user.email.is)),
           "newPw" -> password_*("", LFuncHolder(newPasswd = _)),
           "submit" -> submit("Set Password", testAndSet _),
           "delete" -> submit("Delete User", testAndDelete _)
         )
    }
    
    bind("userlist", xhtml, "entries" -> ((insidexhtml) =>
    User.findAll().flatMap(entry(_, insidexhtml))))
  }
}
