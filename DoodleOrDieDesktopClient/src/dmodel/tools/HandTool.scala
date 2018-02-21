package dmodel.tools
import view.DoodlePanel
import dmodel.Coord

object HandTool extends BasicTool { //(Array())
  
  private var previousCoord = Coord(0,0) //initiate  0,0 coord, it should be replaced once the mouse is pressed anyways
  
  override def getLines() = {collection.mutable.Buffer()} //for redrawing the whole line while drawing?
  override def getLastLine() = {None} // for drawing one segment of the line

  override def onMouseDown(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    dp.prepareMove(coord) //previousCoord = coord
  }
  override def onMouseDrag(dp:DoodlePanel, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
    //var change = coord - previousCoord
    dp.prepareMove(coord)
    //dp.move(coord)
    //previousCoord = coord
  }
  override def onMouseUp(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    //var change = coord - previousCoord
    dp.move(coord)
  }
}