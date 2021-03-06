package dmodel.json

/**
  * Json class for gson to load doodle data from the save file
  *

  * @author Qazhax
  */

import dmodel.dpart._
import dmodel.{Colors, Coord, Layer}

import scala.collection.mutable

class JsonSave {
  var version:Int = _
  var doodle_id:String = _
  var time:Int = _
  var date: String = _
  var user_id: String = _
  var layers:Array[JsonLayer] = Array()
  var v:Int = _
  var d:String = _
  var t:Int = _
  var l:Array[JsonLayer] = Array()
  
  def getVersion: Int = {
    version match { case 0 => v case x => x }
  }
  
  def getDoodleId: String = {
    doodle_id match { case ""|null => d case x => x }
  }
  
  def getTime: Int = {
    time match { case 0 => t case x => x }
  }
  
  def getLayers: Array[_ <: JsonLayer] = {
    layers match { case Array()|null => if(l!=null)l else Array() case x => x }
  }
  
  //def print = println(this)
  def getDoodleLayers: mutable.Buffer[Layer] = {
    
    val buf = collection.mutable.Buffer[Layer]()
    getLayers.foreach { 
      layer =>
        val tmp = new Layer
        tmp.addStrokes(layer.getStrokes.map(_.toDoodlePart))
        try{tmp.setVisibility(layer.getVisible)}catch{case e:NullPointerException=>tmp.setVisibility(true)}
        buf += tmp
    }
    buf
  }
  
  override def toString: String ="version "+getVersion+". doodle_id "+getDoodleId+". time "+getTime+
      ". layers "+getLayers.take(10).mkString(", ")
}

class JsonLayer {
  var strokes:Array[JsonStroke] = Array()
  var visible:Boolean = true
  var s:Array[JsonStroke] = Array()
  var v:Boolean = true
  def getVisible: Boolean = {
    visible match { case true => v case x => x }
  }
  def getStrokes: Array[JsonStroke] = {
    strokes match { case Array()|null => s case x => x }
  }
}
class JsonStroke {
  var linetype:String=_
  var l:String=_
  var strokes:Array[JsonStroke] = _
  var ss:Array[JsonStroke]=_
  var coords:Array[JsonCoord] = Array()
  var path:Array[Double] = _
  var p:Array[Double] = _
  var color:String = _
  var c:String = _
  var size:Double = _
  var s:Double = _
  var text:String=_
  var t:String=_
  var font:String = _
  var f:String = _
  
  def getLines: Array[BasicLine] = {
    toDoodlePart.getLines
  }
  def toDoodlePart:DoodlePart = {
    try{
    linetype = linetype match { case ""|null => l case x => x }
    text = text match { case ""|null => t case x => x }
    font = font match { case ""|null => f case x => x }
    color = color match { case ""|null => if(c!=null)c else "#000" case x => x }
    size = size match { case x if x<=0 => if(s>0)s else 1 case x => x }
    path = path match { case Array()|null => if(p!=null)p else Array() case x => x }
    coords = coords match {
      case Array()|null =>
        val buf = collection.mutable.Buffer[JsonCoord]()
        for (i <- 0 until path.length/2){
          buf += new JsonCoord{
            x = path(2*i)
            y = path(2*i+1)}
        }
        buf.toArray
      case x => x
    }
    strokes = strokes match { case Array()|null => if(ss!=null)ss else Array() case x => x }
    linetype match {
      case "multi"|"m" =>
        val res = new MultiLine
        res.setLines(strokes.flatMap(_.getLines))
        res

      case "text"|"t" =>
        val javacolor = Colors.toColor(color)
        var newcoords = coords.map(x=>x.toCoord)
        val res = new TextLine(javacolor,size)
        res.setFont(font)
        res.text = text
        if (newcoords.length>=1) res.startCorner = newcoords(0)
        if (newcoords.length>=2) res.endCorner = newcoords(1)
        res
      case "bezier"|"b" =>
        val javacolor = Colors.toColor(color)
        val res = new BezierLine(javacolor,size)
        var newcoords = coords.map(x=>x.toCoord)
        if(newcoords.length==0){
          val buf = collection.mutable.Buffer[Coord]()
          for (i <- 0 until path.length/2){
            buf += Coord(path(2*i),path(2*i+1))
          }
          newcoords = buf.toArray
        }
        res.setCoords(newcoords)
        res
      case default => //"n"/"normal"
        val tmp = new JsonLine
        tmp.color = color
        tmp.path = path
        tmp.size = size
        tmp.toDoodlePart
    }
    } catch {
      case e:Throwable =>
        e.printStackTrace()
        new EmptyLine
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