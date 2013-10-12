package ddl

trait DataType
trait NumericDataType extends DataType
case class NumberDataType(size: Int) extends NumericDataType
case object IntDataType extends NumericDataType
