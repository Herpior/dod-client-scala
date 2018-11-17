package view

import java.awt.Graphics2D

import dmodel.tools.ConfigVariable

import scala.swing.event.{MouseDragged, MousePressed}

class ConfigSlider[T](config: ConfigVariable[T])(implicit num: Numeric[T])
  extends DodSlider(
    _=>num.toDouble(config.getVal),
    res=>config.setVal(res.asInstanceOf[T]),
    num.toDouble(config.minVal.getOrElse(0.asInstanceOf[T])),
    num.toDouble(config.maxVal.getOrElse(0.asInstanceOf[T])),
    logOffset = config.logOffset,
    logScale = config.logScale){

  this.listenTo(mouse.clicks)
  this.listenTo(mouse.moves)
  this.reactions += {
    case e:MouseDragged =>
      val x = e.point.getX
      val newValue = valFromX(x)
      config.setVal(newValue.asInstanceOf[T])
      //publish(new controller.SizeChangeEvent(SizeModel.getSize))
      repaint
    case e:MousePressed =>
      val x = e.point.getX
      val newValue = valFromX(x)
      config.setVal(newValue.asInstanceOf[T])
      //publish(new controller.SizeChangeEvent(SizeModel.getSize))
      repaint
  }
}