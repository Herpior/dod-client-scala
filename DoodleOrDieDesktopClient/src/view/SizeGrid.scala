package view

import scala.swing.Action
import scala.swing.Dimension
import scala.swing.GridPanel
import dmodel.SizeModel
import dmodel.Magic

class SizeGrid extends GridPanel(2, 4) {
  
  this.preferredSize = new Dimension(200,100)
  this.maximumSize = this.preferredSize
  this.background = Magic.bgColor

  val buttons = Array.ofDim[SizeButton](8)
  val sizes = Array("XXS", "XS", "S", "M", 
                    "L", "XL", "XXL", "XXXL")
  for(i<-buttons.indices){
    buttons(i) = new SizeButton{
      this.action = new Action(sizes(i)){
        def apply() = pickSize(i)
      }
    }
  }
  this.contents ++= buttons
  
  def pickSize(index:Int){
    SizeModel.number(index)
    publish(new controller.SizeChangeEvent(SizeModel.getSize))
  }
  def checkNewSize {
    buttons.foreach { x => x.unselect }
    val index = SizeModel.sizes.indexOf(SizeModel.getSize)
    if(index>=0){
      buttons(index).select
    }
  }
  
}