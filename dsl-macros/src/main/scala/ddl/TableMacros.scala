package ddl

import scala.reflect.macros.Context
import scala.language.experimental.macros

object TableMacros extends ReflectionUtils {
  def check(t: Table) = macro TableMacros.check_impl
  def check_impl(c: Context)(t: c.Expr[Table]): c.Expr[Unit] = {
    import c.universe._
    //<[ () ]>
    c.abort(c.enclosingPosition, "DOH")
    c.Expr[Unit](Block(List(), Literal(Constant())))
  }

  def create[T: c.WeakTypeTag](c: Context)(n: c.Expr[Named[T]]): c.Expr[Unit] = {
    //http://www.scottlogic.com/blog/2013/06/05/scala-macros-part-1.html
    import c.universe._
    import c.{ universe => u }
    import c.universe.{Name => uName}


    val string_context_name = newTermName("StringContext")
    val table_string_interpolator_name = newTermName("t")

    val looking_for = typeOf[CREATE.TABLE.type].termSymbol.name

    def findTableDeclarations(t: Tree): Boolean = t match {
      case Apply(Select(Ident(_), x), y) if x == looking_for =>
        true
      case _ =>
        false
    }

    //Perhaps use c.eval() so we don't have to require the constant?
    def findTableName(t: Tree): Boolean = t match {
      case
        Apply(
          Select(
            Apply(
              Ident(string_context_candidate),
              List(Literal(Constant(table_name_candidate)))
            ),
            string_interpolator_candidate
          ), _
        )
        if string_context_name == string_context_candidate
        && table_string_interpolator_name == string_interpolator_candidate
      =>
        true
      case _ =>
        false
    }

    def extractTableName(t: Tree): Option[String] = t match {
      case
        Apply(
          Select(
            Apply(
              Ident(_),
              List(Literal(Constant(table_name)))
            ),
            _
          ), _
        )
      =>
        Some(table_name.toString.trim)
      case _ =>
        None
    }

    def cleanTableName(n: String): Option[String] = Some {
      val first = n.head
      val start = if (first.isLetter || '_' == first) first else "_" + first

      start + (
        for(c <- n.tail) yield {
          if (Character.isWhitespace(c))
            '_'
          else
            c
        }
      )
    }

    val table_decls =
      for {
        unit <- c.enclosingRun.units
        body = unit.body
        table_tree = body.find(findTableDeclarations).getOrElse(c.abort(c.enclosingPosition, s"Unable to locate a table given:\n${show(body)}"))
        table_name_tree = table_tree.find(findTableName).getOrElse(c.abort(c.enclosingPosition, s"Unable to locate a table name. Please ensure that the provided name is a string literal without interpolated values. Was given:\n${show(table_tree)}"))
        table_name = extractTableName(table_name_tree).getOrElse(c.abort(c.enclosingPosition, s"Unable to determine the table name given:\n${show(table_name_tree)}"))
        clean_table_name = cleanTableName(table_name).getOrElse(c.abort(c.enclosingPosition, s"Invalid table name: $table_name. Must be a valid identifier."))
      } yield {
        clean_table_name
      }

    val t = table_decls.mkString("\n------------\n")
    println(s"I FOUND: $t")


    //val foos = CREATE TABLE t"abc" (
    //  c"empno" NUMBER(5) PRIMARY KEY,
    //  c"empno2" NUMBER(5) PRIMARY KEY,
    //  PRIMARY KEY(pk"empno", pk"empno2")
    //)

//    val foo =
//      Apply(
//        Select(
//          Ident("ddl.CREATE"),
//          "ddl.CREATE.TABLE")
//        , List(
//          Apply(
//            Apply(
//              Select(
//                Apply(
//                  Ident(newTermName("StringContext")),
//                  List(
//                    Literal(Constant("abc"))
//                  )
//                ),
//                newTermName("t")
//              ),
//              List()
//            ),
//            List(
//              Apply(
//                Select(
//                  Apply(
//                    Select(
//                      Apply(
//                        Select(
//                          Apply(
//                            Ident(newTermName("StringContext")), List(Literal(Constant("empno")))), newTermName("c")), List()), newTermName("NUMBER")), List(Literal(Constant(5)))), newTermName("PRIMARY")), List(Ident(ddl.KEY))), Apply(Select(Apply(Select(Apply(Select(Apply(Ident(newTermName("StringContext")), List(Literal(Constant("empno2")))), newTermName("c")), List()), newTermName("NUMBER")), List(Literal(Constant(5)))), newTermName("PRIMARY")), List(Ident(ddl.KEY))), Apply(Select(Ident(ddl.PRIMARY), newTermName("KEY")), List(Apply(Select(Apply(Ident(newTermName("StringContext")), List(Literal(Constant("empno")))), newTermName("pk")), List()), Apply(Select(Apply(Ident(newTermName("StringContext")), List(Literal(Constant("empno2")))), newTermName("pk")), List())))))))


//    val foo =
//    List(
//      Block(
//        List(
//          ValDef(Modifiers(), newTermName("foos"), TypeTree(), Apply(
//            Select(Ident("ddl.CREATE"), "ddl.CREATE.TABLE"),
//            List(Apply(Apply(
//              Select(Apply(Ident(newTermName("StringContext")),
//                List(Literal(Constant("abc")))), newTermName("t")), List()
//            ),
//              List(
//                Apply(
//                  Select(Apply(Select(
//                  Apply(Select(Apply(
//                    Ident(newTermName("StringContext")), List(Literal(Constant("empno")))), newTermName("c")), List())
//                  , newTermName("NUMBER")), List(Literal(Constant(5)))), newTermName("PRIMARY")), List(Ident("ddl.KEY"))
//                ),
//                Apply(
//                  Select(
//                    Apply(
//                      Select(
//                        Apply(
//                          Select(
//                            Apply(
//                              Ident(
//                                newTermName("StringContext")
//                              ),
//                              List(
//                                Literal(Constant("empno2"))
//                              )
//                            ),
//                            newTermName("c")
//                          ),
//                          List()
//                        ),
//                        newTermName("NUMBER")
//                      ),
//                      List(Literal(Constant(5)))
//                    ),
//                    newTermName("PRIMARY")
//                  ),
//                  List(Ident("ddl.KEY"))
//                ),
//                Apply(
//                  Select(
//                    Ident("ddl.PRIMARY"),
//                    newTermName("KEY")
//                  ),
//                  List(
//                    Apply(
//                      Select(
//                        Apply(
//                          Ident(newTermName("StringContext")),
//                          List(
//                            Literal(Constant("empno"))
//                          )
//                        ),
//                        newTermName("pk")
//                      ),
//                      List()
//                    ),
//                    Apply(
//                      Select(
//                        Apply(
//                          Ident(newTermName("StringContext")),
//                          List(
//                            Literal(Constant("empno2"))
//                          )
//                        ),
//                        newTermName("pk")
//                      ),
//                      List()
//                    )
//                  )
//                )
//              )
//            )
//            )
//          ))), Literal(Constant(()))))

//    val n_raw =
//      for {
//        unit <- c.enclosingRun.units
//        body = unit.body
//      } yield {
//        showRaw(body)
//      }
//
//    println(s"\n\nn (Named[T]):\n------------------\n${n_raw.mkString("\n-------------\n")} \n ")
//
    val case_class_raw = showRaw(reify {
      object MyObject {
        case class MyClass(myParam1: Int, myParam2: Option[String])
      }
    }.tree)
    println(s"\n\nCase class:\n------------------\n$case_class_raw\n \n ")

//    val r = reify {
//      class Foo {
////        case class MyClass(myParam1: Int, myParam2: Option[String])
////        object MyObject
//      }
//      new Foo()
//    }
//    r

    //val q"class $name extends Liftable { ..$body }" = n.tree
    reify(())



//    val memberName = newTypeName("blah")
//    val anon = newTypeName(c.fresh)
//
//    val r = c.Expr(Block(
//      ClassDef(
//        Modifiers(Flag.FINAL), anon, Nil, Template(
//          Nil, emptyValDef, List(
//            constructor(c.universe)
//          )
//        )
//      ),
//      Apply(Select(New(Ident(anon)), nme.CONSTRUCTOR), Nil)
//    ))
//
//    r
  }
}

trait ReflectionUtils {
  def constructor(u: scala.reflect.api.Universe) = {
    import u._

    DefDef(
      Modifiers(),
      nme.CONSTRUCTOR,
      Nil,
      Nil :: Nil,
      TypeTree(),
      Block(
        Apply(
          Select(Super(This(tpnme.EMPTY), tpnme.EMPTY), nme.CONSTRUCTOR),
          Nil
        )
      )
    )
  }
}