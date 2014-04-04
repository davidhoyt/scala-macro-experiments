package misc

object Members {
  import scala.language.experimental.macros

  def iteratePublicMembers[T]: Iterable[String] =
    macro MemberMacros.iteratePublicMembers[T]
}

object MemberMacros {
  import scala.reflect.macros._

  def iteratePublicMembers[T : c.WeakTypeTag](c: blackbox.Context): c.Expr[Iterable[String]] = {
    import c.universe._

    val t = weakTypeOf[T]
    val symbol = t.typeSymbol

    if (!symbol.isClass)
      c.abort(c.enclosingPosition, "Only classes and traits are supported")

    val members = t.decls.collect {
      case decl if decl.isPublic && decl.isMethod && !decl.asMethod.isSetter =>
        q"${decl.name.decodedName.toString}"
    }

    c.Expr[Iterable[String]](q"Seq(..$members)")
  }
}
