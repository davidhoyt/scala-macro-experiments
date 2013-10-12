package ddl

trait DdlTableExpression[+T] extends DdlExpression[T]
trait DdlTableExpressionGroup extends DdlExpressionGroup {
  def expressions: Seq[DdlTableExpression[Any]]
}
trait Table extends DdlTableExpressionGroup
case class TableWithExpressions(expressions: Seq[DdlTableExpression[Any]]) extends Table

trait ColumnModifier
case object PrimaryColumn extends ColumnModifier
case object Nullable extends ColumnModifier

trait ColumnPrimaryConstraint
case object KEY extends ColumnPrimaryConstraint

trait ColumnNullableConstraint
case object NULL extends ColumnNullableConstraint

trait Column extends DdlTableExpression[Column] {
  def name: String
  def dataType: DataType
  def modifiers: Set[ColumnModifier]
  def value: Column = this
}

class ColumnNeedsDataType(val name: String) {
  object NUMBER {
    def apply(value: Int): ColumnWithDataType =
      new ColumnWithDataType(name, NumberDataType(value))
  }
  object INT {
    def apply: ColumnWithDataType =
      new ColumnWithDataType(name, IntDataType)
  }
}

case class ColumnWithDataType(name: String, dataType: DataType, modifiers: Set[ColumnModifier] = Set(Nullable)) extends Column {
  def PRIMARY(constraint: ColumnPrimaryConstraint): ColumnWithDataType =
    constraint match {
      case key =>
        ColumnWithDataType(name, dataType, modifiers + PrimaryColumn)
    }

  def NOT(constraint: ColumnNullableConstraint): ColumnWithDataType =
    constraint match {
      case NULL =>
        ColumnWithDataType(name, dataType, modifiers - Nullable)
    }
}

case class Named[+T](name: String, values: Seq[T]) {
  override def toString = name
}





trait PrimaryKey extends DdlTableExpression[PrimaryKey] {
  def columns: Seq[PrimaryKeyName]
  def value: PrimaryKey = this
}

case class PrimaryKeyName(name: String)
case class PrimaryKeyWithColumns(columns: Seq[PrimaryKeyName]) extends PrimaryKey

object PRIMARY {
  def KEY(cols: PrimaryKeyName*): PrimaryKey =
    PrimaryKeyWithColumns(cols)
}
