//author: Alexander C. Steiner
package org.sombrero.model
 
import net.liftweb.mapper._
import _root_.net.liftweb.common._
import scala.xml._

//Just another random MegaProtoUser implementation.
class User extends MegaProtoUser[User] {
  override def getSingleton = User
    
 
} 

object User extends User with MetaMegaProtoUser[User] {
  override def signupFields =
    if(User.superUser_? || findAll(By(superUser, true)) == Nil)
      List(firstName, lastName, email, password, superUser)
    else
      List(firstName, lastName, email, password)
      
  override def skipEmailValidation = true
  
  override def loginXhtml =
    <lift:surround with="default" at="content">
      { super.loginXhtml }
    </lift:surround>
  
  override def signupXhtml (user : User) =
    <lift:surround with="default" at="content">
      { super.signupXhtml(user) }
    </lift:surround>
      
  override def changePasswordXhtml = {
    def chooseTemplate() = if (User.superUser_?) "default-admin-no-room" else "default-user"
   
    <lift:surround with={chooseTemplate} at="content">
		{ super.changePasswordXhtml }
	</lift:surround>
  }
      
  override def lostPasswordXhtml =
    <lift:surround with="default" at="content">
      { super.lostPasswordXhtml }
    </lift:surround>
      
  override def passwordResetXhtml =
    <lift:surround with="default-user" at="content">
      { super.passwordResetXhtml }
    </lift:surround>
   
  override def editXhtml (user : User) = {
    def chooseTemplate() = if (User.superUser_?) "default-admin-no-room" else "default-user"
    
    <lift:surround with={chooseTemplate} at="content">
    	{ super.editXhtml(user) }
    </lift:surround>
  }
}
