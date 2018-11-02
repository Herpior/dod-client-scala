package view

import scala.swing.Action
import scala.swing.Button
import scala.swing.CheckBox
import scala.swing.Dimension
import scala.swing.FlowPanel
import java.awt.Color
import dmodel.Magic
import dmodel.ToolModel
//import io.Icons

class SubmitPanel extends FlowPanel {

  val model = ToolModel
  model.initReady
  
  if(Magic.offline && !model.isReady) model.clickReady
  
  this.preferredSize = new Dimension(200, 100)
  this.minimumSize = preferredSize
  this.maximumSize = preferredSize
  this.background = Magic.bgColor
  val submitButtText = if(Magic.offline)"Save" else "Submit"
  
  val submitButt = new Button(submitButtText){
    this.font = Magic.font20
    this.preferredSize = new Dimension(200, 50)
    this.background = if(model.isReady) Magic.buttColor else Color.LIGHT_GRAY
    this.foreground = Magic.white
    this.opaque = true
    this.borderPainted = false
    this.action = new Action(submitButtText){
      def apply()=submit
    }
  }
  val readyButt = new CheckBox("Ready to submit"){
    this.font = Magic.font20
    this.preferredSize = new Dimension(200, 50)
    this.background = Magic.bgColor
    this.foreground = Magic.white
    //this.selectedIcon = Icons.getCheckIcon
    this.opaque = true
    this.borderPainted = false
    this.action = new Action("Ready to submit"){
      def apply()=clickReady
    }
  }
  this.contents +=   submitButt
  if(!Magic.offline) {
    this.contents +=   readyButt
  }
  

  private def submit = {
    publish(new controller.SubmitEvent)
  }
  private def clickReady = {
    model.clickReady
    submitButt.background = if(model.isReady) Magic.buttColor else Color.LIGHT_GRAY
    //submitButt.repaint()
  }
  
}