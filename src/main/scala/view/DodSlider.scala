package view

import java.awt.{Graphics2D, RenderingHints}

import dmodel.{Magic, SizeModel}

import scala.swing.{Dimension, Panel}
import scala.swing.event.{MouseDragged, MousePressed}

/* Base class for sliders
 * all logic in Doubles because
 * minval and maxval tell the limits of the slider values
 * logOffset tells where the logscale should start, useful when using integer scale near zero.
 *   increasing it will make the curve more linear, i.e. reduce the scale near the start.
 * logScale tells if the values should use log scale instead of linear scale
 * isInteger tells if the values should be shown as integers or floating point numbers
 */
class DodSlider(getValue:Unit=>Double, setValue:Double=>Unit,/*getString:Unit=>String, */ minVal:Double=0, maxVal:Double=1, logOffset:Double = 1, logScale:Boolean = true, isInteger:Boolean = false) extends Panel{
  val reversed: Boolean = minVal>maxVal


  private val width = 200
  private val maxLog = math.log(maxVal+logOffset)
  private val minLog = math.log(minVal+logOffset)
  private val logArea = maxLog-minLog

  protected val marginSizeX = 1
  protected val marginSizeY = 1
  protected val sliderHeight = 6
  protected val ballSize = 26
  protected val start: Int = marginSizeX +  ballSize/2
  protected val length: Int = width - start*2
  protected val height: Int = ballSize + marginSizeY*2
  def currentValue = getValue()

  this.minimumSize = new Dimension(width, height)
  this.preferredSize = this.minimumSize
  this.maximumSize = this.minimumSize
  this.background = Magic.bgColor

  def valueAsString: String = {
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

  private def valFromNormalizedX(x:Double) = {
    if(logScale) Math.exp((logArea*x)+minLog)-logOffset
    else x*(maxVal - minVal) + minVal
  }
  // maps values in linear range [minVal, maxVal] to logarithmic/linear range [0,1]
  private def normalizedXFromVal(value:Double) = {
    if(logScale)(Math.log(value+logOffset)-minLog)/logArea
    else (value-minVal)/(maxVal-minVal)
  }

  protected def xFromVal(value:Double): Int =  math.round(start + length * normalizedXFromVal(value)).toInt
  protected def valFromX(x:Double): Double = {
    val res = valFromNormalizedX((x-start)/length)
    if(isInteger) math.round(res).toDouble
    else res
  }


}