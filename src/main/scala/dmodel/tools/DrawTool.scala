package dmodel.tools
import dmodel.Coord
import dmodel.ColorModel
import dmodel.SizeModel
import dmodel.Magic
import dmodel.dpart.MultiLine
import dmodel.filters.OneEuroFilter
import view.DoodlePanel

import scala.collection.mutable.Buffer

class DrawTool extends LineTool {

  private var smoothing = true
  private val oneEuroFilter = new OneEuroFilter
  private var prev = Coord(0)

  override def onMouseDrag(dp:view.DoodlePanel, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean){
    if(left){
      if(alt){
        dragLine(coord, control, shift)
        dp.redrawDrawing
      }
      else {
        val processedCoord = updateCoord(coord)
        if((processedCoord-prev).abs.max >= 0.4){
          addLine(ColorModel.getColor, SizeModel.getSize, processedCoord)
          if(ColorModel.getColor.getAlpha==255 || Magic.faster) dp.redrawLast
          else dp.redrawDrawing
        }
      }
      dp.repaint
    }
  }

  override def onMouseUp(dp: DoodlePanel, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    super.onMouseUp(dp, coord, button, control, alt, shift)
    oneEuroFilter.reset
  }

  override def onMouseDown(dp: DoodlePanel, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    val processedCoord = updateCoord(coord)
    prev = processedCoord
    super.onMouseDown(dp, processedCoord, button, control, alt, shift)
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

  def setBeta(newBeta:Double) = {
    this.oneEuroFilter.beta = newBeta
  }
  def multiplyBeta(multiplier:Double) = {
    this.oneEuroFilter.beta *= multiplier
  }
  def setMinCutoff(MinCutoff:Double) = {
    this.oneEuroFilter.mincutoff = MinCutoff
  }
  def multiplyMinCutoff(multiplier:Double) = {
    this.oneEuroFilter.mincutoff *= multiplier
  }
  def getBeta = {
    this.oneEuroFilter.beta
  }
  def getMinCutoff = {
    this.oneEuroFilter.mincutoff
  }

  override def getConfigVariables() = {
    val betaConfig = new DoubleConfigVariable("reduce lag", _=>getBeta, setBeta, Some(1e-50), Some(1), true)
    val mincutConfig = new DoubleConfigVariable("reduce jitter", _=>getMinCutoff, setMinCutoff, Some(1), Some(1e-3), true)
    val unitConfig = new UnitConfigVariable("button test", _=>println("getter"), _=>println("setter"))
    val smoothingConfig = new BooleanConfigVariable("smoothing", _=>this.smoothing, smoothing=_)
    Vector(smoothingConfig, betaConfig, mincutConfig, unitConfig).asInstanceOf[Vector[ConfigVariable]]
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