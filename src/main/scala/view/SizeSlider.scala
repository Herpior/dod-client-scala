package view

import scala.swing.Panel
import scala.swing.Dimension
import scala.swing.event._
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.SizeModel
import dmodel.Magic

class SizeSlider extends DodSlider(_=>SizeModel.getSize, res=>SizeModel.setSize(res.toInt), 1, SizeModel.maxSize, 4,true, true){

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