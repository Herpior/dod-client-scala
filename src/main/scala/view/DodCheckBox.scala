package view

import dmodel.Magic

import scala.swing.{Action, CheckBox, Dimension}

class DodCheckBox(text:String, getValue:Unit=>Boolean, setValue:Boolean=>Unit) extends CheckBox(text){
  this.font = Magic.font20
  this.preferredSize = new Dimension(200, 50)
  this.background = Magic.bgColor
  this.foreground = Magic.white
  //this.selectedIcon = Icons.getCheckIcon
  this.opaque = true
  this.borderPainted = false
  this.selected = getValue()
  def getSelected = this.selected
  this.action = new Action(text){
    def apply()=setValue(getSelected)
  }

}
