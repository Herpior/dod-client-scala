package view

import java.awt.{Graphics2D, RenderingHints}

import dmodel.{Magic, SizeModel}

import scala.swing.{Dimension, Panel}
import scala.swing.event.{MouseDragged, MousePressed}

/* Base class for sliders
 * minval and maxval tell the limits of the slider values
 * logScale tells if the values should use log scale instead of linear scale
 * isInteger tells if the values should be shown as integers or floating point numbers
 */
class DodSlider(minVal:Double, maxVal:Double, logScale:Boolean = true, isInteger:Boolean = false) extends Panel{

  private val width = 200
  private val logArea = math.log(maxVal-minVal+1)

  protected val marginSizeX = 1
  protected val marginSizeY = 1
  protected val sliderHeight = 6
  protected val ballSize = 26
  protected val start = marginSizeX +  ballSize/2
  protected val length = width - start*2
  protected val height = ballSize + marginSizeY*2
  protected var currentValue = minVal

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

  private def realValFromX(x:Double) = {
    Math.exp((x-start)/length*logArea)+minVal-1
  }
  def xFromVal(value:Double) = {
    math.round( start + length*Math.log(value-minVal+1)/logArea ).toInt
  }
  def valFromX(x:Double) = {
    if(isInteger) math.round(realValFromX(x)).toDouble
    else realValFromX(x)
  }


}