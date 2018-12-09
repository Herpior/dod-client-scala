package dmodel

import dmodel.dpart._

import collection.mutable.Buffer

class Layer() {
  protected val redos = Buffer[DoodlePart]()
  //var img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB)
  //var thumb = new BufferedImage(200,150,BufferedImage.TYPE_INT_ARGB)
  protected val strokes = Buffer[DoodlePart]()
  protected var visible = true
  private var selected = false

  //returns false if the old doodlepart is not found
  def swap(findOld:DoodlePart, replaceWith:DoodlePart):Boolean ={
    val editedIndex = strokes.indexOf(findOld)
    if(editedIndex<0) return false
    strokes(editedIndex) = replaceWith
    true
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
  def redoOrReturnLineOnFailure:Option[DoodlePart] = { //used for splitting layer
    if(redos.length>0){
      if(redos.last.onRedo(this)){
        strokes += redos.last
        redos -= redos.last
        None
      } else {
        val res = redos.last
        redos -= res
        Some(res)
      }
    } else None
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
  def reviveAndReturnFailedEditLines= { //used for splitting layer
    val returning = Buffer[DoodlePart]()
    while(redos.length>0){
      redoOrReturnLineOnFailure.foreach(returning += _)
    }
    returning.reverse
  }
  def split = {
    val upper = new Layer
    //if the original line of an editline is not in the redo stack, it's in the undo stack and the edit line should stay in the original layer
    //val editlines = this.redos.filter(dp => dp.isInstanceOf[EditLine] && !this.redos.contains(dp.asInstanceOf[EditLine].originalLine))
    upper.redos ++= this.redos//.filter(!editlines.contains(_))
    this.redos.clear
    val returnedLines = upper.reviveAndReturnFailedEditLines
    this.redos ++= returnedLines
    this.revive
    upper.setVisibility(true)
    upper
  }
  def getStrokes(posting:Boolean)={ //TODO: rename things here
    if(visible)strokes.toArray else Array[DoodlePart]()
  }
  def getRedos={
    redos.toArray
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