package dmodel.tools

// name: name of the variable to be shown in the gui
// getValue: getter function for the value
// setValue: setter function for the value
// minValue: minimum value for values that can have a minimum value and are orderable
// maxValue: maximum value for values that can have a maximum value and are orderable
// logScale: boolean telling whether the scale should be linear or logarithmic for numeric values
class ConfigVariable[T](name:String,getValue:Unit=>T, setValue:T=>Unit, val minVal:Option[T]=None, val maxVal:Option[T]=None, val logScale:Boolean=false, val logOffset:Double = 1e-100)(implicit ord: Ordering[T]) {

  private val reversed = minVal.exists(min=>maxVal.exists(max=>ord.lt(max,min)))
  private val actualMinVal = if(reversed) maxVal else minVal
  private val actualMaxVal = if(reversed) minVal else maxVal

  def getVal = {
    getValue()
  }

  def setVal(x:T): Unit ={
    if(actualMinVal.exists(ord.gt(_,x))) {
      setValue(actualMinVal.getOrElse(x))
    }
    else if(actualMaxVal.exists(ord.lt(_,x))) {
      setValue(actualMaxVal.getOrElse(x))
    }
    else setValue(x)
  }

  def getName = {
    name
  }

  def getTypeString = {
    getValue() match {
      case _:Int =>"Int"
      case _:Double=>"Double"
      case _:String=>"String"
      case _=>"Unknown"
    }
  }
}
