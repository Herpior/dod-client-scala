package dmodel.tools
import view.DoodlePanel
import dmodel.Coord

object ZoomTool extends BasicTool { //(Array())
  
  override def getLines() = {collection.mutable.Buffer()} //for redrawing the whole line while drawing?
  override def getLastLine() = {None} // for drawing one segment of the line

  override def onMouseDown(dp:DoodlePanel, coord:Coord, left:Boolean, right:Boolean, middle:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
    val count = if (control) 1 else 4
    if (left) {
      dp.zoomin(count)//change zoom up
    }
    if (right) {
      dp.zoomin(-count)//change zoom down
    }
  }
}