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
  
  this.minimumSize = new Dimension(200,450)
  this.preferredSize = new Dimension(210,450)
  this.maximumSize = new Dimension(300,4500)
  this.background = Magic.bgColor
  
  this.contents += new SizePanel
  this.contents += new ColorPanel
  this.contents += new ToolPickerPanel
  this.contents += new SubmitPanel
      


      
}