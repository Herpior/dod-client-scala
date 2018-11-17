package view

import java.awt.{Graphics2D, RenderingHints}

import dmodel.{Magic, SizeModel}

import scala.swing.{Dimension, Panel}
import scala.swing.event.{MouseDragged, MousePressed}

/* Base class for sliders
 * all logic in Doubles because
 * minval and maxval tell the limits of the slider values
 * logScale tells if the values should use log scale instead of linear scale
 * isInteger tells if the values should be shown as integers or floating point numbers
 */
class DodSlider(getter:Unit=>Double, minVal:Double=0, maxVal:Double=1, logScale:Boolean = true, isInteger:Boolean = false) extends Panel{
  if (minVal>maxVal)
    throw new IllegalArgumentException("minVal must be smaller than maxVal")

  private val width = 200
  private val logArea = math.log(maxVal-minVal+1)

  protected val marginSizeX = 1
  protected val marginSizeY = 1
  protected val sliderHeight = 6
  protected val ballSize = 26
  protected val start = marginSizeX +  ballSize/2
  protected val length = width - start*2
  protected val height = ballSize + marginSizeY*2
  def currentValue = getter()

  this.minimumSize = new Dimension(width, height)
  this.preferredSize = this.minimumSize
  this.maximumSize = this.minimumSize
  this.background = Magic.bgColor

  def valueAsString = {
    if(isInteger)currentValue.toInt.toString
    else f"$currentValue%1.2f"
  }

  override def paintComponent(g:Graphics2D){
    super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    val pos = xFromVal(currentValue)
    g.setColor(Magic.buttColor)
    g.fillRect(start, height/2-sliderHeight/2, length, sliderHeight)
    //g.fillOval(155, 155, 40, 40)
    g.setColor(Magic.white)
    g.fillOval(pos-ballSize/2, marginSizeY, ballSize, ballSize)
    g.setColor(Magic.buttColor)
    val offset = valueAsString.length()*3+1
    g.setFont(g.getFont.deriveFont(java.awt.Font.BOLD))
    g.drawString(valueAsString, pos-offset, 19)

  }

  private def logValFromX(x:Double) = {
    Math.exp((x-start)/length*logArea)+minVal-1
  }
  private def linearValFromX(x:Double) = {
    ((x-start)/length)+minVal-1
  }

  private def xFromLogVal(value:Double) = {
    start + length*Math.log(value-minVal+1)/logArea
  }
  private def xFromLinearVal(value:Double) = {
    start + length*Math.log(value-minVal+1)/logArea
  }

  def xFromVal(value:Double) = {
    val func = if(logScale) xFromLogVal(_) else xFromLinearVal(_)
    math.round(func(value)).toInt
  }
  def valFromX(x:Double) = {
    val res = if(logScale)logValFromX(x) else linearValFromX(x)
    if(isInteger) math.round(res).toDouble
    else res
  }


}