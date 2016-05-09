package dmodel

class JsonSave {
  var version:Int = _
  var doodle_id:String = _
  var time:Int = _
  var layers:Array[JsonLayer] = Array()
  
  //def print = println(this)
  def getLayers = {
    val buf = collection.mutable.Buffer[Layer]()
    layers.foreach { 
      layer =>
        val tmp = new Layer
        tmp.addStrokes(layer.strokes.map(_.toDoodlePart))
        buf += tmp
    }
    buf
  }
  
  override def toString ="version "+version+". doodle_id "+doodle_id+". time "+time+
      ". layers "+layers.take(10).mkString(", ")
}

class JsonLayer {
  var strokes:Array[JsonStroke] = Array()
}
class JsonStroke {
  var linetype:String=_
  var strokes:Array[JsonLine] = Array()
  var coords:Array[JsonCoord] = Array.fill(4)(new JsonCoord)
  var path:Array[Double] = Array()
  var color:String = _
  var size:Double = _
  def toDoodlePart:DoodlePart = {
    linetype match {
      case "multi" =>
        val res = new MultiLine
        strokes.foreach { x => res.addLine(x.toBasicLine) }
        res
      case "bezier" =>
        val javacolor = Colors.toColor(color)
        val res = new BezierLine(javacolor,size)
        res.setCoords(coords.map(x=>x.toCoord))
        res
      case default =>
        val tmp = new JsonLine
        tmp.color = color
        tmp.path = path
        tmp.size = size
        tmp.toDoodlePart
    }
  }
}/*
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
}*/
class JsonCoord {
  var x:Double = _
  var y:Double = _
  def toCoord = new Coord(x, y)
}