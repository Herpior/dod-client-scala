package view

import dmodel.Magic

import scala.swing.{Action, CheckBox, Dimension}

class DodCheckBox(text:String, getValue:Unit=>Boolean, setValue:Boolean=>Unit) extends CheckBox(text){
  this.font = Magic.font20.deriveFont(16.0f)
  this.preferredSize = new Dimension(200, 20)
  this.minimumSize = new Dimension(200, 20)
  this.background = Magic.bgColor
  this.foreground = Magic.buttColor
  //this.selectedIcon = Icons.getCheckIcon
  this.opaque = true
  this.borderPainted = false
  this.selected = getValue()
  def getSelected = this.selected
  this.action = new Action(text){
    def apply()=setValue(getSelected)
  }

}
