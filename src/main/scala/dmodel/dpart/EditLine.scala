package dmodel.dpart

/**
  * A class representing a single edit in the undo stack.

  * @author Qazhax
  */

import dmodel.Layer


class EditLine(val editedLine:DoodlePart, val originalLine:DoodlePart) extends EmptyLine {

  override def onUndo(layer:Layer): Boolean ={
    layer.swap(editedLine, originalLine)
  }
  override def onRedo(layer:Layer): Boolean ={
    layer.swap(originalLine, editedLine)
  }

}
