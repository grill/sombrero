//author: Alexander C. Steiner
package org.sombrero.model

//Just some utility Regexes used in field validation.
object Matchers {
  lazy val knx = "^[0-9][0-9]?/[0-9][0-9]?/[0-9][0-9]?$".r
  lazy val ip = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$".r
}
