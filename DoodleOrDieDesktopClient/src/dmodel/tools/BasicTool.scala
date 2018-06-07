package dmodel.tools
import view.DoodlePanel
import dmodel.Coord
import dmodel.DoodlePart
import collection.mutable.Buffer

abstract class BasicTool {
  
  def initTool(dp:DoodlePanel) {
    dp.redrawDrawing
    dp.repaint
  }
  def cleanUp(dp:DoodlePanel) {}
  def onMouseDown(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseUp  (dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseDrag(dp:DoodlePanel, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  def onMouseMove(dp:DoodlePanel, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {}
  def getLines():Buffer[DoodlePart] = {collection.mutable.Buffer()} //for drawing the stroke or informational graphics, not for adding strokes to layer
  def getLastLine():Option[DoodlePart] = {None} // for drawing one segment of the stroke

}