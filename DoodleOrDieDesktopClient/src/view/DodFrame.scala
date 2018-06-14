package view

import scala.swing._
import dmodel.Magic

class DodFrame(orig:Component, onClose:Unit=>Unit) extends Frame {
  val decoy = new Label("Don't look at me")
  this.background = Magic.bgColor
  this.iconImage = io.Icons.getDod
  
  this.contents = decoy
  def activate {
    this.contents = orig
    this.visible = true
  }
  def deactivate {
    this.contents = decoy
    this.visible = false
  }
}