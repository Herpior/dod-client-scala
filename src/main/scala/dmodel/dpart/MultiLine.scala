package dmodel.dpart

import dmodel.{Colors, Coord, JsonStroke, Magic}

import scala.collection.mutable.Buffer

// Used as a method to group multiple lines together, e.x. when filling, so they act as a single item in undo stack.
// also an easy intermediate format between the original line format and the doodleordie export/upload format
class MultiLine extends DoodlePart{
  private var lines = Buffer[BasicLine]()
  def transform (transformation:Coord=>Coord) = {
    val next = new MultiLine
    next.setLines(this.lines.flatMap ( line => line.transform(transformation)))
    Some(next)
  }
  def distFrom(point:Coord)={
    val sorted = lines.map(_.distFrom(point)).sorted
    if(sorted.nonEmpty)sorted.head
    else Double.MaxValue // this multiline is empty
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
    lines.lastOption
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
  def compress { //TODO: check that the compress methods in multiline and basicline make sense and are used
    val res = Buffer[BasicLine]()
    val nonempty = lines.filter { !_.getCoords.isEmpty }
    if(nonempty.isEmpty)return
    var curr = nonempty.head
    var cc = curr.getCoords.map(_.rounded(Magic.roundingAccuracy))
    for(st<-nonempty.drop(1)){
      val sc = st.getCoords.map(_.rounded(Magic.roundingAccuracy))
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
    Some(res)
  }

  def toJson = {
    val json = new JsonStroke
    json.strokes = this.getLines.flatMap(_.toJson)
    json.linetype = "multi"
    Some(json)
  }
  def toJsonString = {
    //val lines = this.getLines
    if(lines.length == 1) {
      lines.head.toJsonString
    }
    else {
      Some("{\"linetype\":\"multi\",\"strokes\":["+lines.flatMap(_.toJsonString).mkString(",")+"]}")
    }
  }
  def toShortJsonString = {
    //val lines = this.getLines
    if(lines.length == 1) {
      lines.head.toShortJsonString
    }
    else {
      Some("{\"l\":\"m\",\"ss\":["+lines.flatMap(_.toShortJsonString).mkString(",")+"]}") //TODO: consider changing "ss" into something with only one character? also "l" into "t" for all lines?
    }
  }
}