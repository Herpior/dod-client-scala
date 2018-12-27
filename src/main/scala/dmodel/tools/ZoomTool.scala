package dmodel.tools
import dmodel.DoodleBufferer
import dmodel.Coord

class ZoomTool extends BasicTool { //(Array())
  
  override def getLines() = {collection.mutable.Buffer()}

  override def onMouseDown(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    val count = if (control) 1 else 4
    if (button == 1) {
      db.zoomIn(count)//change zoom up
    }
    if (button == 3) {
      db.zoomIn(-count)//change zoom down
    }
  }
}