package dmodel

import dmodel.dpart.{BasicLine, DoodlePart, JsonDoodle, JsonLine}

import collection.mutable.Buffer

class Layer() {
  val redos = Buffer[DoodlePart]()
  //var img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB)
  //var thumb = new BufferedImage(200,150,BufferedImage.TYPE_INT_ARGB)
  protected val strokes = Buffer[DoodlePart]()
  protected var visible = true
  private var selected = false

  def swap(findOld:DoodlePart, replaceWith:DoodlePart):Unit={
    val editedIndex = strokes.indexOf(findOld)
    if(editedIndex<0) return
    strokes(editedIndex) = replaceWith
  }
  def isVisible = visible
  def isSelected = selected
  def isMatrix = false
  
  def pressPoint(coord:Coord){}
  def dragPoint(coord:Coord){}
  def releasePoint {}
  
  def select = selected = !selected
  def visibility = visible = !visible
  
  def setVisibility(next:Boolean) {visible = next}
  
  def load(doodle:JsonDoodle){
    this.strokes ++= doodle.getStrokes.map(_.toBasicLine)
  }
  def load(arr:Array[JsonLine]){
    this.strokes ++= arr.map(_.toBasicLine)
  }
  def merge(another:Layer){
    this.strokes ++= another.getThumb
    this.redos.clear
    this.redos ++= another.redos
  }
  def add(stroke:DoodlePart){
    strokes += stroke
    //redos--=redos
    redos.clear()
    //println(redos.length)
  }
  def addStrokes(adding:Array[DoodlePart]){
    strokes ++= adding
    redos.clear()
  }
  def undo {
    if(strokes.length>0){
      strokes.last.onUndo(this)
      redos += strokes.last
      strokes -= strokes.last
    }
  }
  def redo {
    if(redos.length>0){
      redos.last.onRedo(this)
      strokes += redos.last
      redos -= redos.last
    }
  }
  def burn {
    while (strokes.length>0){
      undo
    }
  }
  def revive {
    while(redos.length>0){
      redo
    }
  }
  def split = {
    val upper = new Layer
    upper.redos ++= this.redos
    this.redos.clear
    upper.revive
    upper.setVisibility(false)
    upper
  }
  def getStrokes(posting:Boolean)={
    if(visible)strokes.toArray else Array[DoodlePart]()
  }
  def getThumb={
    strokes.toArray
  }
  def toJsonString = {
    "{\"strokes\":["+this.strokes.flatMap(_.toJsonString).mkString(",")+"],\"visible\":"+visible+"}"
  }
  def toShortJsonString = {
    "{\"s\":["+this.strokes.flatMap(_.toShortJsonString).mkString(",")+"],\"v\":"+visible+"}"
  }
}

class MatrixLayer(private val orig:Layer) extends Layer {
  
  private var points = Array(Coord(0,0),Coord(Magic.doodleSize.x,0),Magic.doodleSize,Coord(0,Magic.doodleSize.y))
  private var edges = new BasicLine(Magic.red,1){this.setCoords(points++Array(points.head))}
  private var ind = -1
  
  override def isMatrix = true
  
  override def pressPoint(coord:Coord) {
    val pts = points.map(c=>c.dist(coord)).zipWithIndex
    val res = pts.sortBy(_._1).head
    if (res._1<50){
      ind = res._2
    } else ind = -1
  }
  override def dragPoint(coord:Coord) {
    if(ind>=0){
      setPoint(ind, coord)
    }
  }
  override def releasePoint{
    ind = -1
  }
  
  override def burn {
    undo
  }
  override def undo {
    strokes.clear
    strokes ++= orig.getThumb
    points = Array(Coord(0,0),Coord(Magic.doodleSize.x,0),Coord(0,Magic.doodleSize.y),Magic.doodleSize)
    edges.setCoords(points++Array(points.head))
  }
  override def load(doodle:JsonDoodle){
  }
  override def load(arr:Array[JsonLine]){
  }
  override def getStrokes(posting:Boolean)={
    if(posting) {
      if(visible)strokes.toArray else Array[DoodlePart]()
    }
    else {
      if(visible)strokes.toArray++Array(edges) else Array[DoodlePart]()
    }
  }
  override def add(stroke:DoodlePart){
  }
  def setPoint(ind:Int, c:Coord) {
    if(ind>=0&&ind<4) {
      points(ind) = c
      recalculate
      edges.setCoords(points++Array(points.head))
    }
  }
  private def recalculate {
    strokes.clear
    val transformation = Matrix.transferPoint(Array(Coord(0,0),Coord(Magic.doodleSize.x,0),Magic.doodleSize,Coord(0,Magic.doodleSize.y)), points)
    strokes ++= orig.getThumb.flatMap{
      s1 =>
        s1.transform(transformation)
    }
  }
  def normal = {
    val res = new Layer
    strokes.foreach { stroke => res.add(stroke) }
    res
  }
}