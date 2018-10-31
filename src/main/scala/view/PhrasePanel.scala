package view

import scala.swing._
import dmodel.Magic
import scala.swing

class PhrasePanel extends BoxPanel(Orientation.NoOrientation){
  this.background = Magic.buttColor
  this.border = Swing.EmptyBorder(1, 1, 1, 1)
  /*val extraLabel = new Label{
    this.background = java.awt.Color.YELLOW
    this.font = Magic.font20
  }*/
  val phraseLabel = new Label{
    this.font = Magic.font20
  }
  val desc = new BoxPanel(Orientation.Vertical){
    this.background = Magic.white//bgColor
    this.border = Swing.EmptyBorder(30,30,30,30)
    this.minimumSize = new Dimension(400, 50)
    this.preferredSize = new Dimension(900, 100)
    this.maximumSize = new Dimension(3900, 200)
    val flow = new FlowPanel{
      //this.contents+=extraLabel
      this.contents+=phraseLabel
      this.background = Magic.white
    }
    this.contents += flow
  }
  this.contents += desc
  def setPhrase(phrase:String){
    phraseLabel.text = phrase
  }
  /*def setPhrase(phrase:String,extra:String){
    phraseLabel.text = phrase
    extraLabel.text = extra
  }*/
}