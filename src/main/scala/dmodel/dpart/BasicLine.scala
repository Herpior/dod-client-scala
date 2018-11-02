package dmodel.dpart

import java.awt.Color

import dmodel.{Angle, Colors, Coord, JsonStroke}

import scala.collection.mutable.Buffer


class BasicLine(var color:Color, var size:Double) extends DoodlePart {
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

  def getShortPathString(len:Int) = {
    if(coords.size<=len) coords.map(_.toString).mkString(",")
    else coords.take(len).map(_.toString).mkString(",")+",..."
  }
  override def toString = {
    "BasicLine(size="+size.toInt+", color="+Colors.toHexString(color)+", path=["+getShortPathString(2)+"])"
  }
  def toDodJson = {
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
  def toJsonString = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    "{\"linetype\":\"basic\",\"color\":\""+Colors.toHexRGBA(color)+"\",\"size\":"+sizestr+",\"path\":["+coords.map(_.toShortJsonString).mkString(",")+"]}"
  }
  def toShortJsonString = {
    val sizestr = if(size%1==0)size.toInt.toString else size.toString
    "{\"l\":\"n\",\"c\":\""+Colors.toHexRGBA(color)+"\",\"s\":"+sizestr+",\"p\":["+coords.map(_.toShortJsonString).mkString(",")+"]}"
  }
}