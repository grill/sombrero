//author: Alexander C. Steiner
package org.sombrero.view

import _root_.net.liftweb.util.Helpers
import org.sombrero.snippet._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import _root_.net.liftweb.common._
import Helpers._
import SHtml._
import net.liftweb.http.js.jquery._
import _root_.net.liftweb.mapper._
import _root_.java.sql._
import _root_.scala.xml._
import org.sombrero.util._;

import org.sombrero.model._
 

abstract class UserAccess 
case object UserNotFound extends UserAccess
case object UserPermissionDenied extends UserAccess
case class FullUserAccess(u : User) extends UserAccess

//handles user editing for userlist
class UserLoc extends Loc[UserAccess] {

  def response(path : List[String]) = new RewriteResponse(ParsePath(path, "", true, false), Map.empty, true)

  override def rewrite = Full({
    case RewriteRequest(ParsePath(List("useredit", aid), _, _, _), _, _) => {
      Log.info("rewrite!")
        try {
          val uid = aid.toLong
          User.findAll(By(User.id, uid)) match {
            case List(u) => {
              (response("useredit" :: Nil),
              FullUserAccess(u))
            }
            case _ => {
              (response("useredit" :: Nil),
              UserNotFound)
            }
          }
        } catch { case e : NumberFormatException => (RewriteResponse("useredit" :: Nil),
            UserNotFound) }
    }
  })
  
  override def calcTemplate  = Full(<lift:useredit/>)
  
  object redoSnippet extends RequestVar[Box[(NodeSeq) => NodeSeq]](Empty) 
  
  //basic user form
  def userForm(u : User) = (ignore : NodeSeq) => {
    def validateEdit {
      u.validate match {
        case Nil =>
          u.save
          S.redirectTo("/userlist") 
        case xs => S.error(xs); redoSnippet(Full(realrender _))
      }
    }
    def realrender(ignore : NodeSeq) : NodeSeq = {
      bind("user", User.editXhtml(u),
           "submit" -> SHtml.submit("save changes", validateEdit _))
    }
    realrender(ignore)
  }
   
  def wrapIt(content : NodeSeq)(ignore : NodeSeq) =
    <lift:surround with="default" at="content">
   	  {content}
    </lift:surround>
      
  override def snippets = {    
    case ("useredit", _) if ! User.superUser_? => wrapIt(Text("Permission denied."))
    case ("useredit", Full(FullUserAccess(u))) => redoSnippet.is openOr userForm(u)
    case ("useredit", Full(UserPermissionDenied)) => wrapIt(Text("Permission denied."))
    case ("useredit", _) => wrapIt(Text("User not found."))
  }
  
  override def defaultValue = Full(UserNotFound)
  override def params = Nil
  override def link = new Link("useredit" :: Nil, true)
  override def name = "Users"
  override def text = "Users"

}

