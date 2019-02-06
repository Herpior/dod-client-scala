package dmodel.dpart

import dmodel.Layer


class EditLine(val editedLine:DoodlePart, val originalLine:DoodlePart) extends EmptyLine {

  override def onUndo(layer:Layer) ={
    layer.swap(editedLine, originalLine)
  }
  override def onRedo(layer:Layer) ={
    layer.swap(originalLine, editedLine)
  }

}
