package view

import java.awt.{Graphics2D, RenderingHints}

import dmodel.Magic
import dmodel.tools.DoubleConfigVariable

import scala.swing.event.{MouseDragged, MousePressed}
import scala.swing.{Dimension, Panel}

/* Base class for sliders
 * all logic in Doubles because
 * minval and maxval tell the limits of the slider values
 * logOffset tells where the logscale should start, useful when using integer scale near zero.
 *   increasing it will make the curve more linear, i.e. reduce the scale near the start.
 * logScale tells if the values should use log scale instead of linear scale
 * isInteger tells if the values should be shown as integers or floating point numbers
 */
//getValue:Unit=>Double, setValue:Double=>Unit, /*getString:Unit=>String, */ minVal:Double=0, maxVal:Double=1, logOffset:Double = 1, logScale:Boolean = true, isInteger:Boolean = false
class ConfigSlider(config: DoubleConfigVariable) extends Panel{
  def getValue = config.getVal
  def setValue(res:Double) = config.setVal(res)
  val minVal = config.minVal.getOrElse(0.0)
  val maxVal = config.maxVal.getOrElse(1.0)
  val logOffset = config.logOffset
  val logScale = config.logScale
  val reversed = (minVal>maxVal)


  private val width = 200
  private val maxLog = math.log(maxVal+logOffset)
  private val minLog = math.log(minVal+logOffset)
  private val logArea = maxLog-minLog

  protected val marginSizeX = 1
  protected val marginSizeY = 2
  protected val labelHeight = 10
  protected val sliderHeight = 10
  protected val start = marginSizeX
  protected val length = width - start*2 -2
  protected val height = sliderHeight + marginSizeY*2 + labelHeight
  def currentValue = getValue

  this.minimumSize = new Dimension(width, height)
  this.preferredSize = this.minimumSize
  this.maximumSize = this.minimumSize
  this.background = Magic.bgColor

  def valueAsString = {
    //f"$currentValue%1.2f"
    val curr = currentValue
    val str = curr.toString
    if(curr == math.round(currentValue)){
      math.round(currentValue).toString
    } else if(str.contains("E") && str.length>11){
      str.take(8) + str.dropWhile(_!='E')
    } else if(str.length>8){
      str.take(8)
    } else {
      str
    }
  }

  override def paintComponent(g:Graphics2D){
    super.paintComponent(g)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    val pos = xFromVal(currentValue)
    g.setColor(Magic.buttColor)
    g.fillRect(start, marginSizeY+labelHeight, length+2, sliderHeight)
    //g.fillOval(155, 155, 40, 40)
    g.setColor(Magic.white)
    g.fillRect(start+1, marginSizeY+1+labelHeight, pos-1, sliderHeight-2)
    g.setColor(Magic.darkerColor)
    //val offset = valueAsString.length()*3+1
    g.setFont(g.getFont.deriveFont(java.awt.Font.BOLD))
    //g.drawString(valueAsString, pos-offset, 19)
    g.drawString(config.getName+": "+valueAsString, 0, labelHeight)

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

  protected def xFromVal(value:Double) =  math.round(start + length * normalizedXFromVal(value)).toInt
  protected def valFromX(x:Double) = {
    val res = valFromNormalizedX((x-start)/length)
    res
  }


  this.listenTo(mouse.clicks)
  this.listenTo(mouse.moves)
  this.reactions += {
    case e:MouseDragged =>
      val x = e.point.getX
      val newValue = valFromX(x)
      config.setVal(newValue)
      //publish(new controller.SizeChangeEvent(SizeModel.getSize))
      repaint
    case e:MousePressed =>
      val x = e.point.getX
      val newValue = valFromX(x)
      config.setVal(newValue)
      //publish(new controller.SizeChangeEvent(SizeModel.getSize))
      repaint
  }

}