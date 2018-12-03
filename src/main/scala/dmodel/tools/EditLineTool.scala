package dmodel.tools

import dmodel._
import dmodel.dpart._
import view.DoodlePanel

class EditLineTool extends SelectTool { //(Array())

  override def onMouseUp(dp: DoodlePanel, coord: Coord, button: Int, control: Boolean, alt: Boolean, shift: Boolean): Unit = {
    super.onMouseUp(dp, coord, button, control, alt, shift)
    if(selected.nonEmpty) selected(0) match {
      case (line:MultiLine)=>
        val edited = new MultiLine
        val lines = line.getLines.map{
          subline=>
            val editedSubline = new BasicLine(ColorModel.getColor, subline.size)
            editedSubline.setCoords(subline.getCoords)
            editedSubline
        }
        edited.setLines(lines)
        swap(dp, line, edited)
      case (line:BasicLine)=>
        val edited = new BasicLine(ColorModel.getColor, line.size)
        edited.setCoords(line.getCoords)
        swap(dp, line, edited)
      case (line:BezierLine)=>
        val edited = new BezierLine(ColorModel.getColor, line.size)
        edited.setCoords(line.getCoords)
        swap(dp, line, edited)
      case any => println(any)
    }
    selected = Array()
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