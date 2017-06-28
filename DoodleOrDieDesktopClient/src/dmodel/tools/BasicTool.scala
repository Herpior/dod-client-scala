package dmodel.tools

abstract class BasicTool(val controls:Array[String]) {
  
  def onMouseDown(x:Int, y:Int, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseUp(x:Int, y:Int, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseDrag(x:Int, y:Int, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseMove(x:Int, y:Int, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}

}