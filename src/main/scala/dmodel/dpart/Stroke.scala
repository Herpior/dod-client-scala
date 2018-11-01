package dmodel.dpart

import java.awt.Color

import dmodel._

import scala.collection.mutable.Buffer

/*class TextLine(cornerx:Double,cornery:Double,val color:Color,val size:Double) extends DoodlePart{
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
}*/




/*class Linee extends DoodlePart{
  var line = Buffer[Strooke]()
  
  
  def getLines = line.toArray
}*/

/*class CC[T] {
  def unapply(a:Option[Any]):Option[T] = if (a.isEmpty) {
    None
  } else {
    Some(a.get.asInstanceOf[T])
  }
}

object M extends CC[Map[String, Any]]
object L extends CC[List[Any]]
object S extends CC[String]
object D extends CC[Double]
object B extends CC[Boolean]*/

class Response {//TODO: what is this Response class?
  var descriptor:Map[String, JsonLine] = _
}

class JsonDoodle {
  var version:Int = _
  var doodle_id:String = _
  var user_id:String = _
  var date:String = _
  var time:Int = _
  var count:Int = _
  var width:Int = _
  var height:Int = _
  var ext:String = _
  var url:String =_
  var strokes:Array[JsonLine] = Array()
  
  def getStrokes:Array[JsonLine] = if(strokes.isEmpty)http.HttpHandler.getDoodle(url).getStrokes else strokes
  
  //def print = println(this)
  override def toString ="version "+version+". doodle_id "+doodle_id+". user_id "+user_id+". date "+date+". time "+time+
      ". count "+count+". width "+width+". height "+height+". ext "+ext+". strookes "+strokes.take(10).mkString(", ")
}