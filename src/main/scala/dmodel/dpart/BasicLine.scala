package dmodel.dpart

import java.awt.Color

import dmodel._

import scala.collection.mutable.Buffer


class BasicLine(var color:Color, var size:Double) extends DoodlePart {
  override def length2:Double = {
    if(coords.isEmpty) return 0.0
    var last = coords.head
    var len = 0.0
    for(c<-coords.drop(1)){
      len += last.dist(c)
      last = c
    }
    len
  }
  private var coords = Buffer[Coord]()
  def transform (transformation:Coord=>Coord) = {
    val next = new BasicLine(this.color,this.size)
    next.setCoords(this.coords.map(c=>transformation(c)))
    Some(next)
  }
  def distFrom(point:Coord)={
    if(this.coords.isEmpty){500}
    else this.getCoords.map(_.dist(point)).min
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

  def compress : Array[BasicLine] = { //TODO: check that the compress methods in multiline and basicline make sense and are used
    if (coords.isEmpty) return Array()
    var pc = coords.head.rounded(Magic.roundingAccuracy)
    val lines = Buffer[BasicLine]()
    var tc = new BasicLine(this.color,this.size)//Buffer[Coord](pc)
    tc.addCoord(pc)
    var prevDir = Coord(0)
    //var pk = 10.0//pk = previous angle
    //var ppk = 10.0
    for(i<- coords){
      val c = i.rounded(Magic.roundingAccuracy)
      val dir = c - pc //TODO: test that this works properly
      //val x = math.round(xs(i)*2)/2.0
      //val y = math.round(ys(i)*2)/2.0
      //val k = pc.angle(c)
      val comp = if(prevDir == Coord(0)) 0 else prevDir.angleBetweenVector(dir)//math.abs(Angle.compare(pk,k))
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
        prevDir = dir
      }
      pc = c
    }
    if(!tc.getLastOption.contains(pc)){
      tc.addCoord(pc)
    }
    //this.setCoords(tc)
    lines += tc
    //println(lines.length)
    lines.toArray
  }

  def getShortPathString(len:Int) = {
    if(coords.size<=len) coords.map(_.toString).mkString(",")
    else coords.take(len).map(_.toString).mkString(",")+",..."
  }
  override def toString = {
    "BasicLine(size="+size.toInt+", color="+Colors.toHexString(color)+", path=["+getShortPathString(2)+"])"
  }
  def toDodJson = {
    "{\"path\":["+coords.map{_.rounded(Magic.roundingAccuracy).toShortJsonString
    }.mkString(",")+"],\"size\":"+size.toInt+",\"color\":\""+Colors.toHexString(color)+"\"}"
  }
  def selection = {
    val line = new BasicLine(Colors.inverse(this.color),1)
    line.setCoords(this.getCoords)
    Some(line)
  }
  def toJson = {
    val json = new JsonStroke
    json.color = Colors.toHexString(color)
    json.size = size
    json.path = this.coords.flatMap(_.rounded(Magic.roundingAccuracy).toArray).toArray
    json.linetype = "basic"
    Some(json)
  }
  def toJsonLine = {
    val json = new JsonLine
    json.color = Colors.toHexString(color)
    json.size = size
    json.path = this.coords.flatMap(_.rounded(Magic.roundingAccuracy).toArray).toArray
    Some(json)
  }
  def toJsonString = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    Some("{\"linetype\":\"basic\",\"color\":\""+Colors.toHexRGBA(color)+"\",\"size\":"+sizestr+",\"path\":["+coords.map(_.rounded(Magic.roundingAccuracy).toShortJsonString).mkString(",")+"]}")
  }
  def toShortJsonString = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    Some("{\"l\":\"n\",\"c\":\""+Colors.toHexRGBA(color)+"\",\"s\":"+sizestr+",\"p\":["+coords.map(_.rounded(Magic.roundingAccuracy).toShortJsonString).mkString(",")+"]}")
  }
}