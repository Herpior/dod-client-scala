package view

import scala.swing.BoxPanel
import scala.swing.ScrollPane
import scala.swing.Dialog
import scala.swing.Dimension
import scala.swing.event._
import scala.swing.Orientation
//import scala.swing.Reactor
import dmodel.Magic
import dmodel.ToolModel

class ToolPanel extends ScrollPane {
  
  val model = ToolModel
  
  this.background = Magic.bgColor
  
  val sizeP = new SizePanel
  val colorP = new ColorPanel
  val toolP = new ToolPickerPanel
  val submitP = new SubmitPanel
  
  def setTool(i:Int) {
    toolP.setTool(i)
  }
  
  val box = new BoxPanel(Orientation.Vertical) {
    this.background = Magic.bgColor
    this.minimumSize = new Dimension(200,500)
    this.preferredSize = new Dimension(210,600)
    this.maximumSize = preferredSize
    this.contents += sizeP
    this.contents += colorP
    this.contents += toolP
    this.contents += submitP
  }
  this.contents = box

  this.listenTo(toolP)
  
  this.reactions += {
    case e:controller.ToolChangeEvent =>
      //tool changed, shouldn't need any actions here for now, but could be useful when the tools can have some gui for configuring them and the gui needs to be swapped
  }

      
}