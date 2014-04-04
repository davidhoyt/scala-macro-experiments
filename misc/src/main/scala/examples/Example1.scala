package examples

import misc.Members

trait ExampleTrait {
  def method1: Unit
  def method2: Int
  val val1: String
  var var1: String
  protected val privateVal: Int
}

object Example1 extends App {
  Members.iteratePublicMembers[ExampleTrait] foreach println
}
