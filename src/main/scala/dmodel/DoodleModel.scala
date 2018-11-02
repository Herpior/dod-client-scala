package dmodel

import dmodel.dpart.{BasicLine, DoodlePart, JsonDoodle}

import collection.mutable.Buffer
import http.HttpHandler
import io.LocalStorage
import dmodel.tools.BasicTool

class DoodleModel {
  val layers = new LayerList//Buffer(new Layer)
  val tools = ToolModel
  //private val next = new CurrentLine
  
  //private var multiLine:Option[MultiLine] = None
  //private var bezierLine:Option[BezierLine] = None
  
  //private val currentLines:Buffer[DoodlePart] = Buffer()
  
  private def currentTool:BasicTool = tools.getTool
  private def currentLines:Buffer[DoodlePart] = currentTool.getLines
  private def matrix:Boolean = this.layers.getCurrent.isInstanceOf[MatrixLayer]
  //private var textLine:Option[TextLine] = None
  
  //private var bezier = false
  
  //private var room:String = "global"
  //private var id:String = ""
  private var paintTime = 0
  private def chain = HttpHandler.getChain
  private def room= HttpHandler.getGroup
  
  //private var current = 0
  //private var state = 0
  //private var size = 25
  //private var color = "#000000"
  //private var color2 = "#000000"
  
  //def isWriting = textLine.isDefined
  def isDrawing = tools.getTool.isBusy //currentLines.length>0/*multiLine.isDefined*/ || hoveringLine2.isDefined
  //def isBezier = bezier && bezierLine.isDefined
  def isMatrix = {
    matrix
  }
  //---------\\
  def addTime(time:Long){
    //println(paintTime)
    paintTime += (time/1000).toInt
    if(paintTime < 0) paintTime = 1000000
  }
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
  def startDrawing(dp:DoodlePart){
    this.currentLines.clear()
    this.currentLines += dp
  }
  def startDrawing(dps:Array[DoodlePart]){
    this.currentLines.clear()
    this.currentLines ++= dps
  }
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
    currentLines.toArray//(bezierLine/*++textLine*/++multiLine).toArray
  }
  def getLast = {
  //  multiLine.flatMap(_.getLast.flatMap(_.getLastLine))
    currentTool.getLastLine
  }
  def getLastMid = {
    val strokes = layers.getCurrent.getStrokes(false)//(current).getStrokes
    strokes.lastOption
  }
  def getLayers = {
    layers.toArray
  }
  def getFlatStrokes = {
    layers.toArray.flatMap(_.getStrokes(true).flatMap(_.getLines))
  }
  //---------\\
  def load(loaded:JsonDoodle){
    layers.head.load(loaded)
  }
  def loadSave(chain:String){
    try{
      val loaded = io.LocalStorage.loadSave(chain)//.decryptFrom(path,chain)
      layers.load(loaded)
      //layers.getCurrent/*(this.current)*/.load(loaded._1)
      addTime(loaded.time)
    }
    catch{
      case e:java.io.FileNotFoundException=>
      case e=>e.printStackTrace}
  }
  def decryptFrom(path:String){
    try{
      val loaded = io.LocalStorage.decryptFrom(path)
      layers.addLayers(loaded.getDoodleLayers)
      addTime(loaded.getTime)
    }
    catch{
      case e:java.io.FileNotFoundException=>
      case e=>e.printStackTrace}
  }
  def submit={
    HttpHandler.submitDoodle(this.toDodPostJson)
  }
  def save(chain:String)={
    LocalStorage.saveTo(layers.toShortJsonString(this.getPaintTime, chain), chain)
  }
  //---------\\
  def getPaintPercentage={
    //println("pp "+LineDrawer.paintPercentage(layers.toArray))
    LineDrawer.paintPercentage(layers.toArray)
    //???
  }
  def getPaintTime={
    //println("pt "+paintTime)
    paintTime
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

  def toDodPostJsonStrokes: String = {
    this.toDodPostJsonStrokes(this.getFlatStrokes)
  }
  def toDodPostJsonStrokes(flatStrokes:Array[BasicLine]): String = {
    // TODO: val strokesInDodFormat = flatStrokes.map(_.toDodJson)
     "["+flatStrokes.mkString(",")+"]"
  }
  def toDodPostJson = {
    val flatStrokes = this.getFlatStrokes
    val rawDodPostStrokes = this.toDodPostJsonStrokes(flatStrokes)
    var j = rawDodPostStrokes.length
    var curr = 0

    // the "anti cheating" code on the site,
    // splitting the strokes into certain length parts
    def h(a:Int) = {
      val b = Math.ceil(j * (a / 100.0)).toInt
      //println(j+" - "+b)
      j -= b
      val res = "\""+rawDodPostStrokes.substring(curr,curr+b).replaceAll("\"", "\\\\\"")+"\""
      curr += b
      res
    }
    //val pt = (math.random*100000 + stookes.length()*5).toInt
    // val pt = this.getPaintTime.toInt

    // dividers between the split pieces of the stroke array
    def chpt(a:Int) = ",\""+chain+paintTime+""+a+"\":"
    "{\"chain_id\":\""+chain+"\",\"group_id\":\""+room+"\",\"sc\":"+flatStrokes.length+",\"pt\":"+paintTime+
      ",\"pp\":"+this.getPaintPercentage+",\"ext\":\"true\""+chpt(0)+j+chpt(3)+h(30)+chpt(2)+h(20)+chpt(1)+h(10)+chpt(5)+h(50)+chpt(4)+h(100)+"}"
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