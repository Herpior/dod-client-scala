package dmodel

import collection.mutable.Buffer
import http.HttpHandler
import io.LocalStorage

class DoodleModel {
  val layers = new LayerList//Buffer(new Layer)
  val tools = ToolModel
  //private val next = new CurrentLine
  
  private var multiLine:Option[MultiLine] = None
  private var bezierLine:Option[BezierLine] = None
  private def matrix:Boolean = this.layers.getCurrent.isInstanceOf[MatrixLayer]
  //private var textLine:Option[TextLine] = None
  private var hoveringLine:Option[DoodlePart] = None
  private var hoveringLine2:Option[DoodlePart] = None
  
  private var bezier = false
  
  //private var room:String = "global"
  //private var id:String = ""
  private var painttime = 0
  
  //private var current = 0
  //private var state = 0
  //private var size = 25
  //private var color = "#000000"
  //private var color2 = "#000000"
  
  def selected = (hoveringLine ++ hoveringLine2).toArray
  //def isWriting = textLine.isDefined
  def isDrawing = multiLine.isDefined || hoveringLine2.isDefined
  def isBezier = bezier && bezierLine.isDefined
  def isMatrix = {
    matrix
  }
  //---------\\
  def addTime(time:Long){
    //println(painttime)
    painttime += time.toInt
    if(painttime < 0) painttime = 1000000}
  //def setState(tool:Int) {state = tool}
  //def setSize(next:Int) {size = next}
  /*def setColor(col:String,ind:Int){
    if(ind<1)color = col else color2 = col
  }*/
  //---------\\
  //def getState = state
  //def getSize = size
  //def getColor = color
  //---------\\
  def getTop = {
    layers.getTop//drop(current+1).flatMap { x => x.getStrokes }.toArray
  }
  def getBot = {
    layers.getBot//.take(current).flatMap { x => x.getStrokes }.toArray
  }
  def getMid = {
    layers.getCurrent//(current).getStrokes
  }
  //---------\\
  def getDrawing = {
    (bezierLine/*++textLine*/++multiLine).toArray
  }
  def getLast = {
    multiLine.flatMap(_.getLast.flatMap(_.getLastLine))
  }
  def getLastMid = {
    val strokes = layers.getCurrent.getStrokes(false)//(current).getStrokes
    strokes.lastOption
  }
  def getLayers = {
    layers.toArray
  }
  //---------\\
  def load(loaded:JsonDoodle){
    layers.head.load(loaded)
  }
  def loadFrom(chain:String){
    try{
      val loaded = io.LocalStorage.loadSave(chain)//.decryptFrom(path,chain)
      val parsed = JsonParse.parseSave(loaded)
      layers.load(parsed)
      //layers.getCurrent/*(this.current)*/.load(loaded._1)
      painttime=parsed.time
    }
    catch{
      case e:java.io.FileNotFoundException=>
      case e=>e.printStackTrace}
  }
  def decryptFrom(path:String,chain:String){
    try{
      val loaded = io.LocalStorage.decryptFrom(path,chain)
      layers/*.getCurrent(this.current)*/.addLayers(loaded._1)
      addTime(loaded._2)
    }
    catch{
      case e:java.io.FileNotFoundException=>
      case e=>e.printStackTrace}
  }
  def submit={
    HttpHandler.submitDoodle(this.getPaintPercentage, this.getPaintTime.toInt, layers.toArray.flatMap(_.getStrokes(true).flatMap(_.getLines)))
  }
  def save={
    LocalStorage.saveTo(layers.toShortJsonString(this.getPaintTime), http.HttpHandler.getChain)
  }
  //---------\\
  def getPaintPercentage={
    //println("pp "+LineDrawer.paintPercentage(layers.toArray))
    LineDrawer.paintPercentage(layers.toArray)
    //???
  }
  def getPaintTime={
    //println("pt "+painttime)
    painttime
    //???
  }
  //---------\\
  /*def addChar(c:Char){
    ???
  }
  def addString(str:String){
    ???
  }
  def backspace{
    ???
  }
  def deleteChar{
    ???
  }*/
  //---------\\
  /*def copy{
    ???
  }
  def paste{
    ???
  }*/
  def undo{
    layers.undo
  }
  def redo{
    layers.redo
  }
  def burn{
    layers.burn
  }
  //---------\\
  /*def mouseMoved(place:Coord,mods:Int){
    ???
  }
  def mouseDragged(place:Coord,mods:Int){
    ???
  }
  def mousePressed(place:Coord,mods:Int){
    ???
  }
  def mouseReleased(place:Coord,mods:Int){
    ???
  }*/
  //---------\\
  def mergeLayer{
    layers.mergeLayer
  }
  def addLayer{
    layers.addLayer
  }
  def layerUp{
    layers.layerUp
  }
  def layerDown{
    layers.layerDown
  }
  //---------\\
  def toLocalStorage{
    io.LocalStorage.printFile(layers.toArray.flatMap(_.getStrokes(true).flatMap(_.getLines)),HttpHandler.getChain,this.getPaintTime.toInt, "backup."+HttpHandler.getGroup+".txt")
  }
  //---------\\
  def select(place:Coord,mods:Int,first:Boolean){
    val strokes = if(mods/256%2==1) {
      this.getLayers.flatMap(_.getStrokes(false))
    }
    else this.getMid.getStrokes(false)
    if(strokes.length<1)return
    var curr = strokes(0)
    var best = curr.distFrom(place)
    for(s<-strokes){
      val dist = s.distFrom(place)
      if(dist<best){
        best = dist
        curr = s
      }
    }
    if(best<10){
      if(first)hoveringLine = Some(curr)
      else hoveringLine2 = Some(curr)
    } else {
      if(first) hoveringLine = None
      else hoveringLine2 = None
    }
  }
  def unselect{
    hoveringLine = None
    hoveringLine2 = None
  }
  //---------\\
  def lineFill(mods:Int){
    //println("linefill model")
    if(this.hoveringLine.isDefined && this.hoveringLine2.isDefined){
      //println("both found")
      val res = new MultiLine
      val line1 = this.hoveringLine.get
      val line2 = this.hoveringLine2.get
      if(line1.isInstanceOf[BezierLine] && line2.isInstanceOf[BezierLine]){
        //println("both bez")
        FillTool.combineBezier(line1.asInstanceOf[BezierLine], line2.asInstanceOf[BezierLine], mods, res)
      }
      else {
        FillTool.linearFill(line1,line2,res)
      }
    layers.getCurrent.add(res)
    }
    unselect
  }
  //---------\\
  def startBezier(place:Coord,mods:Int){
    val bez = new BezierLine(tools.getColor,tools.getSize)
    val guide = new MultiLine
    BezierTool.startBezier(bez, guide, place, mods)
    this.bezierLine = Some(bez)
    this.multiLine = Some(guide)
  }
  def setBezier{
    this.bezier = true
  }
  def dragBezier(point:Int,place:Coord,mods:Int){
    bezierLine.foreach{next =>
      multiLine.foreach{guide =>
        BezierTool.dragBezier(point, next, guide, place, mods)
        }
      }
  }
  def stopBezier{
    this.bezier = false
    bezierLine.foreach(next=>this.layers.getCurrent.add(next))
    bezierLine = None
    multiLine = None
  }
  //---------\\
  def startLine(place:Coord,mods:Int){
    val stroke = new MultiLine
    LineTool.startLine(stroke, tools.getColor, tools.getSize, place, mods)
    multiLine = Some(stroke)
  }
  def dragLine(place:Coord,mods:Int){
    if(multiLine.isEmpty){
      startLine(place,mods)
    }
    multiLine.foreach(_.getLast.foreach{
      next =>
        LineTool.dragLine(next, place, mods)
    })
  }
  def addLine(place:Coord,mods:Int){
    multiLine.foreach{
      next =>
        LineTool.addLine(next, tools.getColor, tools.getSize, place, mods)
    }
  }
  /*def addLinePoint{
    multiLine.foreach{
      next =>
        LineTool.addLinePoint(next)
    }
  }*/
  def stopLine(place:Coord,mods:Int){
    multiLine.foreach{
      next =>
        //if(mods/128%2==0)LineTool.addLine(next,tools.getColor, tools.getSize,place,mods)
        //else next.getLast.foreach(last =>LineTool.dragLine(last,place,mods))
        next.compress
        this.layers.getCurrent.add(next)
    }
    multiLine = None
  }
  //---------\\
  def startGradient(place:Coord,mods:Int){
    val stroke = new MultiLine
    LineTool.startLine(stroke, tools.getColor, 1, place, mods)
    multiLine = Some(stroke)
  }
  def fillGradient(border:java.awt.image.BufferedImage,place:Coord,mods:Int){
    val next = new MultiLine
    val vertical = try{
      val line = multiLine.get.getLast.get.getCoords
      val last = line.last
      val first = line.head
      math.abs(first.x-last.x)<math.abs(first.y-last.y)
    } catch {
      case e:Throwable=> mods/64%2==0
    }
    FillTool.fillGradient(next, border, tools.getColor, tools.getColor2, tools.getSize, vertical, place, mods)
    layers.getCurrent.add(next)
    multiLine=None
  }
  def addGradient(place:Coord,mods:Int){
    val next = new MultiLine
    val vertical = try{
      val line = multiLine.get.getLast.get.getCoords
      val last = line.last
      val first = line.head
      math.abs(first.x-last.x)<math.abs(first.y-last.y)
    } catch {
      case e:Throwable=> mods/64%2==0
    }
    FillTool.addGradient(next, tools.getColor, tools.getColor2, tools.getSize, vertical, place, mods)
    layers.getCurrent.add(next)
    multiLine=None
  }
  //---------\\
  /*def startPerspective(place:Coord,mods:Int){
  }*/
  def dragPerspective(place:Coord,mods:Int){
    if(mods/128%2==1)Perspective.setTertiary(place)
    else if(mods/512%2==1)Perspective.setSecondary(place)
    else Perspective.setPrimary(place)
  }
  def removePerspective(mods:Int){
    if(mods/128%2==1)Perspective.removeTertiary
    else if(mods/512%2==1)Perspective.removeSecondary
    else Perspective.removePrimary
  }
  //---------\\
  /*def startWriting(place:Coord){
    ???
  }
  def dragWriting(place:Coord,mods:Int){
    ???
  }
  def stopWriting{
    ???
  }*/
  
  //---------\\
  def matrixLayer {
    if(!matrix)this.layers.addMatrixLayer(this.layers.getCurrent)
    else this.layers.finaliseMatrix
  }
  def startMatrix(place:Coord,mods:Int){
    layers.getCurrent.pressPoint(place)
  }
  def dragMatrix(place:Coord,mods:Int){
    layers.getCurrent.dragPoint(place)
  }
  def stopMatrix{
    layers.getCurrent.releasePoint
  }
}