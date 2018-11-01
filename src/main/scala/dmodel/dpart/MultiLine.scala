package dmodel.dpart

import dmodel.{Colors, Coord, JsonStroke}

import scala.collection.mutable.Buffer

// Used as a method to group multiple lines together, e.x. when filling, so they act as a single item in undo stack.
// also an easy intermediate format between the original line format and the doodleordie export/upload format
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
    json.strokes = this.getLines.map(_.toJson)
    json.linetype = "multi"
    json
  }
  def toJsonString = {
    //val lines = this.getLines
    if(lines.length == 1) {
      lines.head.toJsonString
    }
    else {
      "{\"linetype\":\"multi\",\"strokes\":["+lines.map(_.toJsonString).mkString(",")+"]}"
    }
  }
  def toShortJsonString = {
    //val lines = this.getLines
    if(lines.length == 1) {
      lines.head.toShortJsonString
    }
    else {
      "{\"l\":\"m\",\"ss\":["+lines.map(_.toShortJsonString).mkString(",")+"]}"
    }
  }
}