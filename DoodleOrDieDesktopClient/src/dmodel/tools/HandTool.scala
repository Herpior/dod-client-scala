package dmodel.tools
import view.DoodlePanel
import dmodel.Coord
import dmodel.Magic

object HandTool extends BasicTool { //(Array())
  
  //private var previousCoord = Coord(0,0) //initiate  0,0 coord, it should be replaced once the mouse is pressed anyways
  //private var point = Magic.doodleSize/2
  private var tmp = Coord(0,0) //point
  
  override def getLines() = {collection.mutable.Buffer()} //for redrawing the whole line while drawing?
  override def getLastLine() = {None} // for drawing one segment of the line

  override def onMouseDown(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    tmp = coord
    dp.startMove
  }
  override def onMouseDrag(dp:DoodlePanel, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
    //var change = coord - previousCoord
    //dp.prepareMove(coord)
    //dp.move(coord)
    //previousCoord = coord
    val moved = (tmp-coord)
    if (Magic.fasterPan)
      dp.prepareMove(moved)
    else {
      dp.movePanPoint(moved)
      dp.redrawAll
    }
      
    dp.repaint
    //point = Coord(max(min(moved.x,Magic.x),0),max(min(moved.y,Magic.y),0))
  }
  override def onMouseUp(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    //var change = coord - previousCoord
    val moved = (tmp-coord)
    dp.movePanPoint(moved)
    dp.redrawAll
    dp.repaint
  }
}