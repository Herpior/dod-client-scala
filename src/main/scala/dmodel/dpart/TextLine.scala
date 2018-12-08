package dmodel.dpart

import java.awt.Color

import dmodel.{Coord, Fonts}

// old half-implemented line format for a text tool
/*
class TextLine(cornerx:Double,cornery:Double,val color:Color,val size:Double) extends DoodlePart{
  //var cornerx = 0.0
  //var cornery = 0.0
  def maxLen = 520-cornerx-15*coeff
  var text = ""
  var font = 0
  var coeff = 1.0
  //var size = 1.0
  //var color = "#000"
  def distFrom(point:Coord) = {
    this.toMultiLine.distFrom(point)
  }
  def toMultiLine:MultiLine={
    var offsetx = 0.0
    var offsety = 0.0
    var lines = new MultiLine
    for(c<-text){
      val l = Fonts.getLetter(c, font)
      //println(l.xs(0).mkString(","))
      for(i<-l.coords){
        val stroke = new BasicLine(color,size)
        stroke.setCoords(i.toBuffer)
        //stroke.xs ++= l.xs(i).map(z=>cornerx+(z + offsetx)*coeff)
        //stroke.ys ++= l.ys(i).map(z=>cornery+(z + offsety)*coeff)
        lines.addLine( stroke )
      }
      offsetx += l.l
      if(offsetx*coeff>maxLen){
        offsetx = 0
        offsety += 25
      }
      if((offsety)*coeff+cornery>390)
        return lines
    }
    //println("instroke "+this.cornerx)
    lines
  }
  override def getLines = this.toMultiLine.getLines
  //def toNextLinee={
  // val n = new nextLinee
  // this.getNextLines.foreach{ x => n.strookes += x }
  //}
}
*/