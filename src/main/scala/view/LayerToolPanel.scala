package view

import scala.swing._
import scala.swing.event.MouseReleased
import java.awt.Graphics2D
import java.awt.RenderingHints

import controller.RepaintEvent
import dmodel.Magic
import dmodel.LayerList

class LayerToolPanel(model:LayerList) extends GridPanel(2,3){
//  this.preferredSize = new Dimension(150, 45)
  this.preferredSize = new Dimension(150, 45)
  this.maximumSize = this.preferredSize
  this.background = Magic.bgColor
  private val parent = this
  
  def publishRepaint(): Unit = this.publish(new RepaintEvent)
  
  val addButt: LayerButton = new LayerButton{
    this.action = new Action("Add"){
      def apply(): Unit = {
        model.addLayer
        publishRepaint
      }
    }
  }
  val mergeButt: LayerButton = new LayerButton{
    this.action = new Action("Merge"){
      def apply(): Unit = {
        val input = Dialog.showOptions(parent, "Are you sure you want to merge layer to the layer below?", "Confirmation", Dialog.Options.YesNo, Dialog.Message.Question, null, Seq(Dialog.Result.Yes, Dialog.Result.Cancel), 0)
        if(input == Dialog.Result.Yes){
          model.mergeLayer
          publishRepaint
        }
      }
    }
  }
  val remoButt: LayerButton = new LayerButton{
    this.action = new Action("Remove"){
      def apply(): Unit = {
        val input = Dialog.showOptions(parent, "Are you sure you want to remove layer? this can't be undone.", "Confirmation", Dialog.Options.YesNo, Dialog.Message.Question, null, Seq(Dialog.Result.Yes, Dialog.Result.Cancel), 0)
        if(input == Dialog.Result.Yes){
          model.removeLayer
          publishRepaint
        }
      }
    }
  }
  val undoButt: LayerButton = new LayerButton{
    this.action = new Action("Undo"){
      def apply(): Unit = {
        model.undo
        publishRepaint
      }
    }
  }
  val redoButt: LayerButton = new LayerButton{
    this.action = new Action("Redo"){
      def apply(): Unit = {
        model.redo
        publishRepaint
      }
    }
  }
  val burnButt: LayerButton = new LayerButton{
    this.action = new Action("Burn"){
      def apply(): Unit = {
        val input = Dialog.showOptions(parent, "Are you sure you want to undo everything on the layer?", "Confirmation", Dialog.Options.YesNo, Dialog.Message.Question, null, Seq(Dialog.Result.Yes, Dialog.Result.Cancel), 0)
        if(input == Dialog.Result.Yes){
          model.burn
          publishRepaint
        }
      }
    }
  }
  
  this.contents += addButt
  this.contents += mergeButt
  this.contents += remoButt
  this.contents += undoButt
  this.contents += redoButt
  this.contents += burnButt
  /*
  override def paintComponent(g:Graphics2D){
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(Magic.bgColor)
    g.fillRect(0, 0, this.bounds.getWidth.toInt, this.bounds.getHeight.toInt)
    g.setColor(Magic.buttColor)
    g.fillRoundRect(2, 2, 36, 18, 4, 4)
    g.fillRoundRect(42, 2, 42, 18, 4, 4)
    g.fillRoundRect(88, 2, 50, 18, 4, 4)
    g.setColor(Magic.white)
    g.setFont(g.getFont.deriveFont(java.awt.Font.BOLD))
    g.drawString("add", 10, 15)
    g.drawString("merge", 45, 15)
    g.drawString("remove", 92, 15)
  }*/
  /*
  this.listenTo(mouse.clicks)
  this.reactions += {
    case e:MouseReleased =>
        val x = e.point.getX
        if(x>=0&&x<40){
          //println("layerlist curr"+model.ind)
          //println("layerlist curr"+model.ind)
        }else if(x>=40&&x<86){
          this.model.mergeLayer
          publish(new RepaintEvent)
        }else if(x>=86&&x<134){
          this.model.removeLayer
          publish(new RepaintEvent)
        }
  }
  */
}