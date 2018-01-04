package dmodel.tools
import view.DoodlePanel
import dmodel.Coord
import dmodel.Perspective

object PerspectiveTool extends LineToolClass {

  
  override def initTool() {}
  override def cleanUp() {}
  override def onMouseDown(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {}
  override def onMouseUp  (dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {}
  override def onMouseDrag(dp:DoodlePanel, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {}
  override def onMouseMove(dp:DoodlePanel, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {}
  
}