package dmodel.tools

/**
  * A tool that can be used to draw freehand lines, smoothed by 1€ filter.

  * @author Qazhax
  */

import dmodel._
import dmodel.filters.OneEuroFilter


class DrawTool extends LineTool {

  private var smoothing = true
  private val oneEuroFilter = new OneEuroFilter
  private var prev = Coord(0)
  private var minAddEpsilon = 0.4

  override def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean){
    if(left){
      if(alt){
        dragLine(coord, control, shift)
        db.redrawDrawing
      }
      else {
        val processedCoord = updateCoord(coord)
        if((processedCoord-prev).abs.max >= minAddEpsilon){
          addLine(getColor, SizeModel.getSize, processedCoord)
          if(getColor.getAlpha==255 || Magic.faster) db.redrawLast
          else db.redrawDrawing
        }
      }
      //db.repaint
    }
  }

  override def onMouseUp(db: DoodleBufferer, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    super.onMouseUp(db, coord, button, control, alt, shift)
    oneEuroFilter.reset
  }

  override def onMouseDown(db: DoodleBufferer, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    val processedCoord = updateCoord(coord)
    prev = processedCoord
    super.onMouseDown(db, processedCoord, button, control, alt, shift)
  }

  private def updateCoord(coord:Coord) = {
    if(smoothing) {
      val timestamp = System.nanoTime()*1e-9
      oneEuroFilter.update(coord, timestamp)
    }
    else {
      coord
    }
  }

  def setBeta(newBeta:Double): Unit = {
    this.oneEuroFilter.beta = newBeta
  }
  def setMinCutoff(minCutoff:Double): Unit = {
    this.oneEuroFilter.mincutoff = minCutoff
  }
  def setDCutoff(dCutoff:Double): Unit = {
    this.oneEuroFilter.dcutoff = dCutoff
  }
  def getBeta: Double = {
    this.oneEuroFilter.beta
  }
  def getMinCutoff: Double = {
    this.oneEuroFilter.mincutoff
  }
  def getDCutoff: Double = {
    this.oneEuroFilter.dcutoff
  }

  override def getConfigVariables(): Vector[ConfigVariable] = {
    //val betaConfig = new DoubleConfigVariable("reduce lag", _=>getBeta, setBeta, Some(1e-50), Some(1), true)
    val mincutConfig = new DoubleConfigVariable("reduce lag", _=>getMinCutoff, setMinCutoff, Some(1e-1), Some(16), true)
    val minaddConfig = new DoubleConfigVariable("minimum line segment length", _=>this.minAddEpsilon, minAddEpsilon=_, Some(1e-1), Some(1), true)
    //val dcutConfig = new DoubleConfigVariable("dcutoff", _=>getDCutoff, setDCutoff, Some(100), Some(1e-3), true)
    //val unitConfig = new UnitConfigVariable("button test", _=>println("getter"), _=>println("setter"))
    val smoothingConfig = new BooleanConfigVariable("use smoothing", _=>this.smoothing, smoothing=_)
    val transparencyConfig = new DoubleConfigVariable("transparency", _=>transparency, transparency=_, Some(0.0), Some(1.0), false)
    Vector(smoothingConfig, mincutConfig, transparencyConfig, minaddConfig)//.asInstanceOf[Vector[ConfigVariable]]
  }

  //---------\\
  /*
  def startLine(place:Coord,mods:Int){
    //val stroke = new MultiLine
    LineTool.startLine(ColorModel.getColor, SizeModel.getSize, place, mods)
    //multiLine = stroke
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
        LineTool.addLine(next, ColorModel.getColor, SizeModel.getSize, place, mods)
    }
  }
  */
  /*def addLinePoint{
    multiLine.foreach{
      next =>
        LineTool.addLinePoint(next)
    }
  }*/
  
  
}