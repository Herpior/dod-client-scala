package dmodel.tools

import dmodel.{Colors, Coord, DoodleBufferer}
import dmodel.dpart.{BezierLine, DoodlePart, MultiLine}

class BezierInterpolationTool extends SelectTool {

  override def onMouseUp  (db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    // if selected and hovering both full, and different lines, do the do
    if(lineFill(db, control, alt, shift)) {
      db.redrawLastMid
      db.redrawDrawing
      //db.repaint
    }
  }
  override def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
    if(left && selectOne(db, coord, control, alt)) {
      db.redrawDrawing
      //db.repaint
    }
  }
  
  // returns true if the fill is successful
  def lineFill(db:DoodleBufferer, control:Boolean, alt:Boolean, shift:Boolean)={
    var success = false
    if(selected.nonEmpty && hovering.nonEmpty){
      //println("both found")
      val res = new MultiLine
      val line1 = hovering.get
      val line2 = selected.get
      if(line1.isInstanceOf[BezierLine] && line2.isInstanceOf[BezierLine]){
        //println("both bez")
        combineBezier(line1.asInstanceOf[BezierLine], line2.asInstanceOf[BezierLine], control, alt, shift, res)
        success = true
      }
      else {
        linearFill(line1,line2,res)
      }
      db.model.layers.getCurrent.add(res)
    }
    unselect
    success
  }
  
  def combineBezier(first:BezierLine,second:BezierLine,control:Boolean, alt:Boolean, shift:Boolean ,res:MultiLine){
    val len = math.max(first.length2,second.length2)
    var max = 0
    for(i<-0 to len.toInt){
      val t = 1.0/i
      val dist = first.getCoordAt(t).dist(second.getCoordAt(t)).toInt
      if(dist>max) max = dist
    }
    var tmp = 0.0
    val mid = (math.abs(first.size+second.size)/2).toInt
    val n = (2.0*max/mid).toInt
    val colors = Colors.linearcolor(n,alt,first.color,second.color)
    for(i<- 1 until n){
      val in = i*1.0/n//tmp/max
      val nin = 1-in
      val color = colors(i)
      val size = mid//(in*first.size + nin*second.size).toInt
      //tmp += size/2
      val c0 = first.getCoord(0) * in + second.getCoord(0) * nin
      val c1 = first.getCoord(1) * in + second.getCoord(1) * nin
      val c2 = first.getCoord(2) * in + second.getCoord(2) * nin
      val c3 = first.getCoord(3) * in + second.getCoord(3) * nin
      val bez = new BezierLine(color,size)
      bez.setCoord(0, c0)
      bez.setCoord(1, c1)
      bez.setCoord(2, c2)
      bez.setCoord(3, c3)
      res.addLine(bez.getLine(color, size))
    }
  }
  def linearFill(first:DoodlePart,second:DoodlePart,res:MultiLine){
    //TODO make this work maybe
  }
  
}