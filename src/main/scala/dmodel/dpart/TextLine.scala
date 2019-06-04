package dmodel.dpart

/**
  * An unused class representing one line of text.
  * old half-implemented line format for a text tool.
  *
  * @author Qazhax
  */

import java.awt.Color

import dmodel.json.JsonStroke
import dmodel.{Colors, Coord, Fonts, Magic}

class TextLine(color:Color,val size:Double) extends DoodlePart{
  //var cornerx = 0.0
  //var cornery = 0.0
  var startCorner = Coord(0)
  var endCorner = Magic.doodleSize
  def maxLen = endCorner.x-startCorner.x-15*coeff
  var text = ""
  var font = new dmodel.Font("default", 0)
  var coeff = 1.0
  //var size = 1.0
  //var color = "#000"

  def setFont(fontName:String){
    font = Fonts.getFont(fontName)
  }

  def distFromEdge(point:Coord) = {
    this.toMultiLine.distFromEdge(point)
  }
  def distFromCenter(point:Coord) = {
    this.toMultiLine.distFromCenter(point)
  }

  override def selection: Option[DoodlePart] = this.toMultiLine.selection
  override def transform(transformation: Coord => Coord) = this.toMultiLine.transform(transformation)
  override def toSVGString: String = this.toMultiLine.toSVGString
  override def length2: Double = this.toMultiLine.length2

  override def toShortJsonString: Option[String] = {
    val linetypePart = "\"l\":\"t\""
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    val sizePart = "\"s\":"+sizestr
    val colorPart = "\"c\":\""+Colors.toHexRGBA(color)+"\""
    val fontPart = "\"f\":\""+font.name+"\""
    val textPart = "\"t\":\""+text+"\""
    val allParts = Array(linetypePart,sizePart,colorPart,fontPart,textPart)
    Some("{"+allParts.mkString(",")+"}")
  }
  override def toJsonString: Option[String] = {
    val linetypePart = "\"linetype\":\"text\""
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    val sizePart = "\"size\":"+sizestr
    val colorPart = "\"color\":\""+Colors.toHexRGBA(color)+"\""
    val fontPart = "\"font\":\""+font.name+"\""
    val textPart = "\"text\":\""+text+"\""
    val allParts = Array(linetypePart,sizePart,colorPart,fontPart,textPart)
    Some("{"+allParts.mkString(",")+"}")
  }

  def toMultiLine:MultiLine={
    var offsetx = 0.0
    var offsety = 0.0
    var lines = new MultiLine
    for(c<-text){
      val l = Fonts.getLetter(c, font.index)
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
      if((offsety)*coeff+startCorner.y>endCorner.y)
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