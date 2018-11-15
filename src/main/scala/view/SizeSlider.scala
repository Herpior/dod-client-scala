package view

import scala.swing.Panel
import scala.swing.Dimension
import scala.swing.event._
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.SizeModel
import dmodel.Magic

class SizeSlider extends DodSlider(1, SizeModel.maxSize, true, true){

  override def paintComponent(g: Graphics2D): Unit = {
    this.currentValue = SizeModel.getSize
    super.paintComponent(g)
  }

  this.listenTo(mouse.clicks)
  this.listenTo(mouse.moves)
  this.reactions += {
    case e:MouseDragged =>
      val x = e.point.getX
      val sizeValue = valFromX(x).toInt
      //if(changingSize){
      SizeModel.setSize(sizeValue)
      publish(new controller.SizeChangeEvent(SizeModel.getSize))
    //repaint
    //this.nextsize = min(max(x.toInt,1),200)
    //}
    case e:MousePressed =>
      val x = e.point.getX
      val sizeValue = valFromX(x).toInt
      SizeModel.setSize(sizeValue)
      publish(new controller.SizeChangeEvent(SizeModel.getSize))
    //repaint
  }
}