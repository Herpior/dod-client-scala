package view

import scala.swing.BoxPanel
import scala.swing.Dialog
import scala.swing.Dimension
import scala.swing.event._
import scala.swing.Orientation
//import scala.swing.Reactor
import dmodel.Magic
import dmodel.ToolModel

class ToolPanel extends BoxPanel(Orientation.Vertical){
  
  val model = ToolModel
  
  this.minimumSize = new Dimension(200,500)
  this.preferredSize = new Dimension(210,550)
  this.maximumSize = new Dimension(300,4500)
  this.background = Magic.bgColor
  
  val sizeP = new SizePanel
  val colorP = new ColorPanel
  val toolP = new ToolPickerPanel
  val submitP = new SubmitPanel
  
  this.contents += sizeP
  this.contents += colorP
  this.contents += toolP
  this.contents += submitP
      


      
}