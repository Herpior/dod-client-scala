package dmodel.tools

import dmodel.DoodleBufferer
import dmodel.{Coord, DoodleBufferer, Magic}

class HandTool extends BasicTool { //(Array())
  
  //private var previousCoord = Coord(0,0) //initiate  0,0 coord, it should be replaced once the mouse is pressed anyways
  //private var point = Magic.doodleSize/2
  private var tmp = Coord(0,0) //point

  override def onMouseDown(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    tmp = coord
    db.startMove
  }
  override def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
    //var change = coord - previousCoord
    //db.prepareMove(coord)
    //db.move(coord)
    //previousCoord = coord
    val moved = tmp-coord
    if (Magic.fasterPan)
      db.prepareMove(moved)
    else {
      db.movePanPoint(moved)
      db.redrawAll
    }
      
    //db.repaint
    //point = Coord(max(min(moved.x,Magic.x),0),max(min(moved.y,Magic.y),0))
  }
  override def onMouseUp(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    //var change = coord - previousCoord
    val moved = (tmp-coord)
    db.movePanPoint(moved)
    db.redrawAll
    //db.repaint
  }
}