package dmodel.tools
import dmodel.Coord
import dmodel.ColorModel
import dmodel.SizeModel
import dmodel.Magic
import dmodel.dpart.MultiLine

class DrawTool extends LineTool {
 

  override def onMouseDrag(dp:view.DoodlePanel, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean){
    if(left){
      if(alt){
        dragLine(coord, control, shift)
        dp.redrawDrawing
      }
      else {
        addLine(ColorModel.getColor, SizeModel.getSize, coord)
        //println("doodlingpanel drag pen tool alpha:"+tools.model.getColor.getAlpha)
        if(ColorModel.getColor.getAlpha==255 || Magic.faster) dp.redrawLast
        else dp.redrawDrawing
      }
      dp.repaint
    }
  }
  //---------\\
  /*
  def startLine(place:Coord,mods:Int){
    //val stroke = new MultiLine
    LineTool.startLine(ColorModel.getColor, SizeModel.getSize, place, mods)
    //multiLine = stroke
  }
  def dragLine(place:Coord,mods:Int){
    if(multiLine.isEmpty){
      startLine(place,mods)
    }
    multiLine.foreach(_.getLast.foreach{
      next =>
        LineTool.dragLine(next, place, mods)
    })
  }
  def addLine(place:Coord,mods:Int){
    multiLine.foreach{
      next =>
        LineTool.addLine(next, ColorModel.getColor, SizeModel.getSize, place, mods)
    }
  }
  */
  /*def addLinePoint{
    multiLine.foreach{
      next =>
        LineTool.addLinePoint(next)
    }
  }*/
  
  
}