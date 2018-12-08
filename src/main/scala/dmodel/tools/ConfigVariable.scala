package dmodel.tools

// name: name of the variable to be shown in the gui
// getValue: getter function for the value
// setValue: setter function for the value
// minValue: minimum value for values that can have a minimum value and are orderable
// maxValue: maximum value for values that can have a maximum value and are orderable
// logScale: boolean telling whether the scale should be linear or logarithmic for numeric values

// if T = Unit, this can be used to create a button
// if T = Boolean, this can be used to create a checkbox
// if T = Double, this can be used to create a slider
// if T = Int, this can be used to create a slider
//class NumberConfigVariable(name:String,getValue:Unit=>Int, setValue:Int=>Unit, val minVal:Option[Int]=None, val maxVal:Option[Int]=None, val logScale:Boolean=false, val logOffset:Double = 1e-100)(implicit ord: Ordering[Int]) extends ConfigVariable(name) {
abstract class NumConfigVariable[T](name:String,getValue:Unit=>T, setValue:T=>Unit, val minVal:Option[T]=None, val maxVal:Option[T]=None, val logScale:Boolean=false, val logOffset:Double = 1e-100)(implicit ord: Ordering[T]) extends ConfigVariable(name) {

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
}
class IntConfigVariable(name:String,getValue:Unit=>Int, setValue:Int=>Unit, minVal:Option[Int]=None, maxVal:Option[Int]=None, logScale:Boolean=false, logOffset:Double = 1e-100) extends NumConfigVariable(name, getValue, setValue, minVal, maxVal, logScale, logOffset) {

}
class DoubleConfigVariable(name:String,getValue:Unit=>Double, setValue:Double=>Unit, minVal:Option[Double]=None, maxVal:Option[Double]=None, logScale:Boolean=false, logOffset:Double = 1e-100) extends NumConfigVariable(name, getValue, setValue, minVal, maxVal, logScale, logOffset) {

}

class UnitConfigVariable(name:String,getValue:Unit=>Unit, setValue:Unit=>Unit) extends ConfigVariable(name) {
  def getVal = getValue()
  def setVal = setValue()
}
class BooleanConfigVariable(name:String,getValue:Unit=>Boolean, setValue:Boolean=>Unit) extends ConfigVariable(name) {
  def getVal = getValue()
  def setVal(b:Boolean) = setValue(b)
}
abstract class ConfigVariable(name:String) {

  def getName = {
    name
  }
/*
  def getTypeString = {
    getValue() match {
      case _:Int =>"Int"
      case _:Double=>"Double"
      case _:String=>"String"
      case _:Unit=>"Unit"
      case _:Boolean=>"Boolean"
      case _=>"Unknown"
    }
  }*/
}
