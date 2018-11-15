package dmodel.dpart

import dmodel.{Layer}


class EditLine(editedLine:DoodlePart, originalLine:DoodlePart) extends EmptyLine {

  override def onUndo(layer:Layer): Unit ={
    layer.swap(editedLine, originalLine)
  }
  override def onRedo(layer:Layer): Unit ={
    layer.swap(originalLine, editedLine)
  }

}
