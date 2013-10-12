package ddl

trait DdlExpression[+T] {
  def value: T
}

trait DdlExpressionGroup {
  def expressions: Seq[DdlExpression[Any]]
}
