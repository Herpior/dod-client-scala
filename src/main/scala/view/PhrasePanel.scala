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
  /*val phraseLabel = new Label{
    this.font = Magic.font20
    //this.preferredSize =  new Dimension(900, 200)
  }*/
  val flow = new FlowPanel(){//new GridPanel(2, 1){
    //this.contents+=extraLabel
    //this.contents+=phraseLabel
    this.background = Magic.white
  }
  val desc = new BoxPanel(Orientation.Vertical){
    this.background = Magic.white//bgColor
    this.border = Swing.EmptyBorder(30,30,30,30)
    this.minimumSize = new Dimension(400, 50)
    this.preferredSize = new Dimension(900, 100)
    this.maximumSize = new Dimension(3900, 200)

    this.contents += flow
  }
  this.contents += desc
  def setPhrase(phrase:String){
    flow.contents.clear()
    flow.contents +=  new Label(phrase){this.font = Magic.font20}//new scala.swing.TextArea(phrase){this.font = Magic.font20}
    //phrase.split(' ').foreach(word=>flow.contents += new Label(word){this.font = Magic.font20})
    //phraseLabel.text = phrase
  }
  /*def setPhrase(phrase:String,extra:String){
    phraseLabel.text = phrase
    extraLabel.text = extra
  }*/
}