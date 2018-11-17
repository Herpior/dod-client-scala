package dmodel.tools

// name: name of the variable to be shown in the gui
// getValue: getter function for the value
// setValue: setter function for the value
// minValue: minimum value for values that can have a minimum value and are orderable
// maxValue: maximum value for values that can have a maximum value and are orderable
// logScale: boolean telling whether the scale should be linear or logarithmic for numeric values
class ConfigVariable[T](name:String,getValue:Unit=>T, setValue:T=>Unit, val minVal:Option[T]=None, val maxVal:Option[T]=None, val logScale:Boolean=false)(implicit ord: Ordering[T]) {

  def getVal = {
    getValue()
  }

  def setVal(x:T): Unit ={
    if(minVal.exists(ord.gt(_,x))) {
      setValue(minVal.getOrElse(x))
    }
    else if(maxVal.exists(ord.lt(_,x))) {
      setValue(maxVal.getOrElse(x))
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
