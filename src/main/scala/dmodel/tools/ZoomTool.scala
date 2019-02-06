package dmodel.tools
import dmodel.DoodleBufferer
import dmodel.Coord
import dmodel.dpart.DoodlePart

import scala.collection.mutable

class ZoomTool extends BasicTool { //(Array())
  
  override def getLines(): mutable.Buffer[DoodlePart] = {collection.mutable.Buffer()}

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