package dmodel.tools
import view.DoodlePanel
import dmodel.Coord
import dmodel.DoodlePart
import collection.mutable.Buffer

abstract class BasicTool {
  
  def initTool() {}
  def cleanUp() {}
  def onMouseDown(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseUp(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseDrag(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseMove(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  def getLines():Buffer[DoodlePart] {} //for redrawing the whole line while drawing?
  def getLastLine():Option[DoodlePart] {} // for drawing one segment of the line

}