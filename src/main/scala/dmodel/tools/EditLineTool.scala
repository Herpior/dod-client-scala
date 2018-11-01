package dmodel.tools

import dmodel._
import view.DoodlePanel

object EditLineTool extends SelectToolClass { //(Array())

  override def onMouseUp(dp: DoodlePanel, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    super.onMouseUp(dp, coord, button, control, alt, shift)
    selected match {
      case Some(line:MultiLine)=>
        val edited = new MultiLine
        val lines = line.getLines.map{
          subline=>
            val editedSubline = new BasicLine(ColorModel.getColor, subline.size)
            editedSubline.setCoords(subline.getCoords)
            editedSubline
        }
        edited.setLines(lines)
        swap(dp, line, edited)
      case Some(line:BasicLine)=>
        val edited = new BasicLine(ColorModel.getColor, line.size)
        edited.setCoords(line.getCoords)
        swap(dp, line, edited)
      case any => println(any)
    }
    selected = None
  }

  def swap(dp:DoodlePanel, line:DoodlePart, edited:DoodlePart): Unit ={

    dp.model.layers.getCurrent.swap(line, edited)
    val editLine = new EditLine(edited, line)
    dp.model.layers.getCurrent.add(editLine)
    dp.redrawMid
    dp.redrawDrawing
    dp.repaint()
  }

}