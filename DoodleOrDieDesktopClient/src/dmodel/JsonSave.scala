package dmodel

class JsonSave {
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
  var layers:Array[JsonLayer] = Array()
  
  //def print = println(this)
  override def toString ="version "+version+". doodle_id "+doodle_id+". user_id "+user_id+". date "+date+". time "+time+
      ". count "+count+". width "+width+". height "+height+". ext "+ext+". layers "+layers.take(10).mkString(", ")
}

class JsonLayer {
  var strokes:Array[JsonStroke] = Array()
}
trait JsonStroke {
  var linetype:String
  def toDoodlePart:DoodlePart
}
class JsonMultiLine extends JsonStroke {
  var linetype = "multi"
  var strokes:Array[JsonLine] = Array()
  def toDoodlePart = toMultiLine
  def toMultiLine = {
    val res = new MultiLine
    strokes.foreach { x => res.addLine(x.toBasicLine) }
    res
  }
}
class JsonBezierLine extends JsonStroke {
  var linetype = "bezier"
  var coords:Array[JsonCoord] = Array.fill(4)(new JsonCoord)
  var color:String = _
  var size:Double = _
  def toDoodlePart = toBezierLine
  def toBezierLine =  {
    val javacolor = Colors.toColor(color)
    val res = new BezierLine(javacolor,size)
    res.setCoords(coords.map(x=>x.toCoord))
    res
  }
}
class JsonCoord {
  var x:Int = _
  var y:Int = _
  def toCoord = new Coord(x, y)
}