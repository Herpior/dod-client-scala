package dmodel.dpart

import dmodel.Layer

/**
  * A class combining multiple Editlines into a single doodlepart
  *
  * @author Qazhax
  */
class MultiEditLine (val editLines:Array[EditLine]) extends EmptyLine {

  override def onUndo(layer:Layer): Boolean ={
    var res = false
    editLines.foreach(el => res = el.onUndo(layer) || res)
    res
  }
  override def onRedo(layer:Layer): Boolean ={
    var res = false
    editLines.foreach(el => res = el.onRedo(layer) || res)
    res
  }

}
