package view

import swing.Frame
import swing.FlowPanel
import swing.Button
import swing.Dimension
import swing.Action
import swing.Label
import swing.TextField
import java.text.Format
import dmodel.Magic
import dmodel.Coord

object ConfigDrawingPanel extends Frame {
  val confwindow = this
  title   = "Select drawing area size" //Select drawing area size
  /*def findCenter(window: Window) = {
    new Point(window.location.x + window.size.width/2, window.location.y + window.size.height/2)
  }
  location = findCenter(secondFrame.owner)*/
  val xField = new TextField(Magic.doodleSize.x.toInt.toString()){this.preferredSize = new Dimension(50, 20)}
  val yField = new TextField(Magic.doodleSize.y.toInt.toString()){this.preferredSize = new Dimension(50, 20)}
    
  contents = new FlowPanel {
    this.background = Magic.bgColor
    contents += new Label("Width: ")
    contents += xField
    contents += new Label("Height: ")
    contents += yField
    contents += new DodButton{action = Action("Ok") {submit}}
    contents += new DodButton{action = Action("Cancel") {confwindow.dispose()}}
  }
  //size = new Dimension(200,400)
  
  def submit {
    try{
      val x = xField.text.toInt
      val y = yField.text.toInt
      Magic.setXY(x, y)
      publish(new RepaintEvent)
      confwindow.dispose()
    } catch {
      case e:Throwable => //TODO warn that this fails 
    }
  }
  def activate {
    this.visible = true
    xField.text = Magic.doodleSize.x.toInt.toString()
    yField.text = Magic.doodleSize.y.toInt.toString()
  }
  
  this.centerOnScreen()
  xField.requestFocusInWindow()
}
