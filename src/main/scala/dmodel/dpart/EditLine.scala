package dmodel.dpart

import dmodel.{Layer}


class EditLine(val editedLine:DoodlePart, val originalLine:DoodlePart) extends EmptyLine {

  override def onUndo(layer:Layer): Unit ={
    layer.swap(editedLine, originalLine)
  }
  override def onRedo(layer:Layer): Unit ={
    layer.swap(originalLine, editedLine)
  }

}
