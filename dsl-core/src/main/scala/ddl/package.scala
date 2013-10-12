package object ddl {
  implicit class NamedStringInterpolator(val context : StringContext) {
    def t[T <: DdlExpression[Any]](args : Any*)(values: T*): Named[T] =
      Named(context.s(args : _*), values)

    def c(args: Any*): ColumnNeedsDataType =
      new ColumnNeedsDataType(context.s(args : _*))

    def pk(args: Any*): PrimaryKeyName =
      new PrimaryKeyName(context.s(args : _*))
  }
}
