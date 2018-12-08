package dmodel.tools
import view.DoodlePanel
import dmodel.Coord

class ZoomTool extends BasicTool { //(Array())
  
  override def getLines() = {collection.mutable.Buffer()}

  override def onMouseDown(dp:DoodlePanel, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    val count = if (control) 1 else 4
    if (button == 1) {
      dp.zoomIn(count)//change zoom up
    }
    if (button == 3) {
      dp.zoomIn(-count)//change zoom down
    }
  }
}