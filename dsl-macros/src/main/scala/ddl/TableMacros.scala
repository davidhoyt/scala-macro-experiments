package ddl

import scala.reflect.macros._
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

class Schema extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro TableMacros.schema
}

object TableMacros extends ReflectionUtils {
  def check(t: Table): Unit = macro TableMacros.check_impl
  def check_impl(c: blackbox.Context)(t: c.Expr[Table]): c.Expr[Unit] = {
    import c.universe._
    //<[ () ]>
    c.abort(c.enclosingPosition, "DOH")
    reify(())
    //c.Expr[Unit](Block(List(), Literal(Constant())))
  }

  def schema(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    import c.{ universe => u }

    println("HELLO??")
    val inputs = annottees.map(_.tree).toList

    reify(())
  }

  def create[T: c.WeakTypeTag](c: blackbox.Context)(n: c.Expr[Named[T]]): c.Expr[Unit] = {
    //http://www.scottlogic.com/blog/2013/06/05/scala-macros-part-1.html
    import c.universe._
    import c.{ universe => u }
    import c.universe.{Name => uName}

    val named_string_interpolator_term = TermName("NamedStringInterpolator")
    val scala_term = TermName("scala")
    val string_context_name = TermName("StringContext")
    val apply_term = TermName("apply")
    val table_string_interpolator_name = TermName("t")

    def findTableName(t: Tree): Boolean = t match {
      case q"${_}.NamedStringInterpolator(scala.StringContext.apply(${_})).t" =>
        true
      case _ =>
        false
    }

    def extractTableName(t: Tree): Option[String] = t match {
      case
        q"${_}(${_}(${Literal(Constant(table_name: String))})).${_}"
        // Select(Apply(Select(_, _), List(Apply(_, List(Literal(Constant(table_name)))))), _)
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
        body <- Some(n.tree)
        table_name_tree = body.find(findTableName).getOrElse(c.abort(c.enclosingPosition, s"Unable to locate a table name. Please ensure that the provided name is a string literal without interpolated values. Was given:\n${show(body)}"))
        table_name = extractTableName(table_name_tree).getOrElse(c.abort(c.enclosingPosition, s"Unable to determine the table name given:\n${show(table_name_tree)}"))
        clean_table_name = cleanTableName(table_name).getOrElse(c.abort(c.enclosingPosition, s"Invalid table name: $table_name. Must be a valid identifier."))
      } yield {
        clean_table_name
      }

    val t = table_decls.mkString("\n------------\n")
    println(s"I FOUND: $t")


    //val foos = CREATE TABLE t"abc" (
    //  c"em=[],
    // ='
    // ])PO?uk ibojkuo7,im8o9nphmpno" NUMBER(5) PRIMARY KEY,
    //  c"empno2" NUMBER(5) PRIMARY KEY,
    //  PRIMARY KEY(pk"empno", pk"empno2")
    //)

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
    //println(s"\n\nCase class:\n------------------\n$case_class_raw\n \n ")

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
//  def constructor(u: scala.reflect.api.Universe) = {
//    import u._
//
//    DefDef(
//      Modifiers(),
//      termNames.CONSTRUCTOR,
//      Nil,
//      Nil :: Nil,
//      TypeTree(),
//      Block(
//        Apply(
//          Select(Super(This(typeNames.EMPTY), typeNames.EMPTY), termNames.CONSTRUCTOR),
//          Nil
//        )
//      )
//    )
//  }
}