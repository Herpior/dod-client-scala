package view

/*import scala.swing.Frame
import javafx.scene.control.ColorPicker
import javafx.scene.paint.Color
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.Group
import javafx.stage.Stage
import javafx
import dmodel.Colors

class ColourPickerFrame(orig:java.awt.Color)(op:java.awt.Color=>Unit) extends JFXPanel {
  
  private var picker:ColorPicker = null
  
  
 override def start(stage:Stage){
    picker = new ColorPicker(Colors.toColor(orig))
    picker.setOnAction(new javafx.event.EventHandler[javafx.event.ActionEvent]{
    def handle(e:javafx.event.ActionEvent){
      val c = picker.getValue
      op(Colors.toColor(c))
      //frame.close()
      //model.setColor(dmodel.Colors.toHexString(c))
    }
  })
  val scene = new Scene(picker, 400, 300)
  stage.setTitle("Color picker")
  stage.setScene(scene)
  stage.show()
 }
  
  //val frame = this
  
  //picker.setValue(javafx.scene.paint.Color.web(model.getColor))
  //picker.seto
  
}*/

import dmodel.Magic
import scala.swing._

class ColourPickerFrame(orig:java.awt.Color)(op:java.awt.Color=>Unit) extends Frame {
  this.background = Magic.bgColor
  this.iconImage = io.Icons.getDod
  val picker = new ColorChooser
  picker.background = Magic.bgColor
  
  this.contents = /*new FlowPanel(*/picker//)
}