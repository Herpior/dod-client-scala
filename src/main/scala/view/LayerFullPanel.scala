package view

import scala.swing._
import java.awt.BasicStroke
import java.awt.image.BufferedImage
import java.awt.Color
import dmodel.Magic
import dmodel.LayerList

class LayerFullPanel(model:LayerList) extends BoxPanel(Orientation.Vertical) {
  
  this.background = Magic.bgColor

  val tools = new LayerToolPanel(model)
  val list = new LayerListPanel(model)
  
  this.contents += tools
  this.contents += new ScrollPane(list){
    this.minimumSize=new Dimension(150,400)
    this.horizontalScrollBarPolicy = scala.swing.ScrollPane.BarPolicy.Never
    }
  
  def reset = list.reset
  
  this.deafTo(this)
  this.listenTo(tools)
  this.listenTo(list)
  
  this.reactions += {
    case e:RepaintEvent=>
      list.reset
      publish(new RepaintEvent)
      this.repaint
  }
  
}