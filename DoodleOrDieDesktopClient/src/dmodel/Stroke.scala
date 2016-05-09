package dmodel

import collection.mutable.Buffer
import collection.mutable.Stack
import java.awt.image.BufferedImage
import java.awt.Color

trait DoodlePart{
  def distFrom(point:Coord):Double
  def getLines:Array[BasicLine]
  def transform(transformation:Coord=>Coord):DoodlePart
  def length2:Double = getLines.foldLeft(0.0)(_+_.length2)
  def selection:DoodlePart
  def toJson:JsonStroke
}

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

class BezierLine(val color:Color, val size:Double) extends DoodlePart {
  //var size = 1.0
  //var color = "#000"
  private val coords = Array.fill(4)(Coord(0,0))
  require (coords.length==4)
  //val xs = Array(0.0,0.0,0.0,0.0)
  //val ys = Array(0.0,0.0,0.0,0.0)
  /*var x2 = 0.0
  var y2 = 0.0
  var x3 = 0.0
  var y3 = 0.0
  var x4 = 0.0
  var y4 = 0.0*/
  def transform (transformation:Coord=>Coord):BezierLine = {
    val next = new BezierLine(this.color,this.size)
    next.setCoords(this.coords.map(c=>transformation(c)))
    next
  }
  def getCoordAt(dist:Double):Coord={
    if(dist<=0)return coords.head
    if(dist>=1)return coords.last
    Bezier.pointAt(dist,coords(0),coords(1),coords(2),coords(3))
  }
  def getCoord (ind:Int)={
    if(ind>=0&&ind<4)
    coords(ind) else coords(0)
  }
  def setCoord(ind:Int,place:Coord){
    if(ind>=0&&ind<4)
    coords(ind) = place
  }
  def setCoords(places:Array[Coord]){
    val maxi = math.min( places.length,4)
    for(i<-0 until maxi){
      coords(i) = places(i)
    }
  }
  def distFrom(point:Coord)={
    (this.coords++this.getLine(color,size).getCoords).map(_.dist(point)).sorted.head
  }
  def getLine(color1:Color,size1:Double):BasicLine={
    if(coords.forall(_==coords(0))){
      val st = new BasicLine(color1,size1)
      st.setCoords( Array(coords(0)) )
      return st
    }
    val cs = Bezier.curve(coords(0),coords(1),coords(2),coords(3))
    val res = new BasicLine(color1,size1)
    res.setCoords (cs.map { c => Coord(math.round(c.x*2)/2.0,math.round(c.y*2)/2.0) })
    res
  }
  def getLines : Array[BasicLine]= {
    val res = this.getLine(color,size)
    //res.xs = xys._1.map(x=>math.round(2*x)/2.0)
    //res.ys = xys._2.map(y=>math.round(2*y)/2.0)
    Array(res)
  }
  def selection = {
    val res = new MultiLine
    res.addLine(this.getLine(Colors.inverse(color),1))
    val line = new BasicLine(Color.black,1)
    line.setCoords(coords)
    res.addLine(line)
    res
  }
  def toJson = {
    val json = new JsonStroke
    json.color = Colors.toHexString(color)
    json.size = size
    json.coords = coords.map(_.toJson)
    json.linetype = "bezier"
    json
  }
}


class MultiLine extends DoodlePart{
  private var lines = Buffer[BasicLine]()
  def transform (transformation:Coord=>Coord):MultiLine = {
    val next = new MultiLine
    next.setLines(this.lines.map ( line => line.transform(transformation)))
    next
  }
  def distFrom(point:Coord)={
    val sorted = lines.map(_.distFrom(point)).sorted
    if(sorted.length>0)sorted.head
    else 500
  }
  def apply(index:Int)={
    lines(index)
  }
  def size={
    lines.length
  }
  def isEmpty={
    lines.isEmpty
  }
  def setLines(arr:Array[BasicLine]){
    lines = arr.toBuffer
  }
  def setLines(arr:Buffer[BasicLine]){
    lines = arr
  }
  def addLine(line:BasicLine){
    lines += line
  }
  def getLines={
    lines.toArray
  }
  def getLast={
    if(lines.isEmpty)None
    else Some(lines.last)
  }
  /*def lineType={
    "multi"
  }*/
  //def first(color:String,size:Double,x0:Double,y0:Double){
  //  lines += new BasicLine(color,size){
  //    xs += x0
  //    ys += y0
  //  }
  //}
  def compress {
    val res = Buffer[BasicLine]()
    val nonempty = lines.filter { !_.getCoords.isEmpty }
    if(nonempty.isEmpty)return
    var curr = nonempty(0)
    var cc = curr.getCoords.map(_.rounded(2))
    for(st<-nonempty.drop(1)){
      val sc = st.getCoords.map(_.rounded(2))
      if( st.size==curr.size &&
          st.color == curr.color &&
          cc.last == sc.head)
      {
        curr.setCoords( cc ++ sc.drop(1) )
        cc=curr.getCoords
      } else {
        res += curr
        curr = st
        cc = sc
      }
    }
    res += curr
    lines = res/*.map(_.clip)*/.flatMap(_.compress)//remove to keep accuracy
    //lines = res
    //println(res.mkString("\n"))
  }
  def selection = {
    val res = new MultiLine
    lines.foreach { x => 
      val line = new BasicLine(Colors.inverse(x.color),1)
      line.setCoords(x.getCoords)
      res.addLine(line)}
    res
  }
  
  def toJson = {
    val json = new JsonStroke
    json.strokes = this.getLines.map(_.toJsonLine)
    json.linetype = "multi"
    json
  }
}

class BasicLine(val color:Color, val size:Double) extends DoodlePart {
  override def length2:Double = {
    if(coords.length==0) return 0.0
    var last = coords.head
    var len = 0.0
    for(c<-coords.drop(1)){
      len += last.dist(c)
      last = c
    }
    len
  }
  private var coords = Buffer[Coord]()
  def transform (transformation:Coord=>Coord):BasicLine = {
    val next = new BasicLine(this.color,this.size)
    next.setCoords(this.coords.map(c=>transformation(c)))
    next
  }
  def distFrom(point:Coord)={
    if(this.coords.length==0){500}
    else {this.getCoords.map(_.dist(point)).sorted.head}
  }
  def setCoords(buf:Buffer[Coord]){ coords = buf }
  def setCoords(arr:Array[Coord]){ coords = arr.toBuffer }
  def getCoords = coords.toArray
  def addCoord(c:Coord){ coords += c }
  def getLines = Array(this)
  def getLast ={this.coords.last}
  def getLastOption ={this.coords.lastOption}
  def getLastLine = {if(coords.length>1) Some(new BasicLine(color,size){this.setCoords(coords.takeRight(2))}) else None}
  def setLast(coord:Coord){
    this.coords(coords.length-1) = coord
  }
  def setCoord(ind:Int,coord:Coord){
    if(ind>=0&&ind<coords.length)
      coords(ind)=coord
  }
  /*
  //clips off parts of line outside the canvas
  //duplicates some points, use compress afterwards
  //TODO make it work
  def clip = {
    val lines = Buffer[BasicLine]()
    var tc = new BasicLine(this.color,this.size)
    for(edge <- coords.sliding(2)){
      var prevcoord = edge(0)
      var currcoord = edge(1)
      var coeff = 1.0
      def prevXOut = prevcoord.x<0  || prevcoord.x>Magic.x
      def prevYOut = prevcoord.y<0  || prevcoord.y>Magic.y
      def currXOut = currcoord.x<0  || currcoord.x>Magic.x
      def currYOut = currcoord.y<0  || currcoord.y>Magic.y
      
      if((prevcoord.x<0 && currcoord.x<0) || (prevcoord.y<0 && currcoord.y<0) ||
      (prevcoord.x>Magic.x && currcoord.x>Magic.x) || (prevcoord.y>Magic.y && currcoord.y>Magic.y)) {
        //no way they intersect the canvas
      }
      else {
        if(prevXOut){
          val moveto = if(prevcoord.x<0) 0 else Magic.x
          coeff = (prevcoord.x-moveto)/(currcoord.x-prevcoord.x)//(currcoord.x-moveto)/(currcoord.x-prevcoord.x)
          //println(coeff)
          prevcoord = prevcoord*(1-coeff)+currcoord*(1-coeff)
        }/*
        if(prevYOut){
          val moveto = if(prevcoord.y<0) 0 else Magic.y
          coeff = (currcoord.y-moveto)/(currcoord.y-prevcoord.y)
          println(coeff)
          prevcoord = prevcoord*coeff+currcoord*(1-coeff)
        }*/
        if(!prevXOut && !prevYOut) tc.addCoord(prevcoord)
        /*
        if(currXOut){
          val moveto = if(currcoord.x<0) 0 else Magic.x
          coeff = (prevcoord.x-moveto)/(prevcoord.x-currcoord.x)
          println(coeff)
          currcoord = currcoord*coeff+prevcoord*(1-coeff)
        }
        if(currYOut){
          val moveto = if(currcoord.y<0) 0 else Magic.y
          coeff = (prevcoord.y-moveto)/(prevcoord.y-currcoord.y)
          println(coeff)
          currcoord = currcoord*coeff+prevcoord*(1-coeff)
        }*/
        if(!currXOut && !currYOut) tc.addCoord(currcoord)
      }
    }
    tc
  }*/
  
  def compress : Array[BasicLine] = {
    if (coords.length == 0) return Array()
    var pc = coords(0).rounded(2)
    val lines = Buffer[BasicLine]()
    var tc = new BasicLine(this.color,this.size)//Buffer[Coord](pc)
    tc.addCoord(pc)
    var pk = 10.0//pk = previous angle
    //var ppk = 10.0
    for(i<- coords){
      val c = i.rounded(2)//Coord(math.round(i.x*2)/2.0,math.round(i.y*2)/2.0)
      //val x = math.round(xs(i)*2)/2.0
      //val y = math.round(ys(i)*2)/2.0
      val k = pc.angle(c)
      val comp = math.abs(Angle.compare(pk,k))
      //val comp2 = math.abs(Angle.compare(ppk,k))
      if(pc==c/*||(comp<math.Pi/32&&this.size==1)*/){ 
        //there are two coordinates at the same point, keep the previous angle and don't add the same coordinate again
        //ppk = pk
      }
      else {
        //if there is a very sharp corner, JOIN_ROUND will fail to create a round edge that I need this to have
        //to prevent this, duplicate the coordinate at the corner.
        //this will notice sharp corners one coordinate later than the corner so we add previous coordinate pc to the line.
        if(comp>math.Pi*31/32){
          //lines += tc
          //tc = new BasicLine(this.color,this.size)
          tc.addCoord( pc )
        }
        tc.addCoord( c )
        //ppk = pk
        pk = k
      }
      pc = c
    }
    if(tc.getLastOption != Some(pc)){
      tc.addCoord(pc)
    }
    //this.setCoords(tc)
    lines += tc
    //println(lines.length)
    lines.toArray
  }
  
  override def toString = {
    "{\"path\":["+coords.map{
    c=>
      val tx = math.round(c.x*2)/2.0
      val x = if(math.round(tx)/1.0 == tx)math.round(tx).toString else tx.toString
      val ty = math.round(c.y*2)/2.0
      val y = if(math.round(ty)/1.0 == ty)math.round(ty).toString else ty.toString
      x+","+y
    }.mkString(",")+"],\"size\":"+size.toInt+",\"color\":\""+Colors.toHexString(color)+"\"}"
  }
  def selection = {
    val line = new BasicLine(Colors.inverse(this.color),1)
    line.setCoords(this.getCoords)
    line
  }
  def toJson = {
    val json = new JsonStroke
    json.color = Colors.toHexString(color)
    json.size = size
    json.path = this.coords.flatMap(_.toArray).toArray
    json.linetype = "basic"
    json
  }
  def toJsonLine = {
    val json = new JsonLine
    json.color = Colors.toHexString(color)
    json.size = size
    json.path = this.coords.flatMap(_.toArray).toArray
    json
  }
}
class JsonLine extends DoodlePart {
  
  var color:String = _
  var size:Double = _
  var path:Array[Double]= Array()
  
  def transform (transformation:Coord=>Coord):BasicLine = {
    val next = this.toBasicLine
    next.transform(transformation)
  }
  def distFrom(point:Coord)={
    this.toBasicLine.distFrom(point)
  }
  def getLines = {
    Array(this.toBasicLine)
  }
  def toDoodlePart = toBasicLine
  def toBasicLine={
    val res = new BasicLine(Colors.toColor(this.color),this.size)
    val buf = Buffer[Coord]()
    for(i<-0 until this.path.length/2){
      buf += Coord(this.path(i*2),this.path(i*2+1))
    }
    res.setCoords(buf)
    res
  }
  def selection = {
    this.toBasicLine.selection
  }
  def toJson = {
    val res = new JsonStroke
    res.color = color
    res.linetype = "basic"
    res.path = path
    res.size = size
    res
  }
}
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