package ddl

import scala.language.experimental.macros

object CREATE {
    object TABLE {
//      def apply[T <: DdlTableExpression[Any]](n: Named[T]): Table = {
//        val table = TableWithExpressions(n.values)
//        TableMacros.check(table)
//        table
//      }

      def apply[T <: DdlTableExpression[Any]](n: Named[T]): Unit =
        macro TableMacros.create[T]
    }
  }