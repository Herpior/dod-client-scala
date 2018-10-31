package dmodel
import java.awt.Color

object Smudge {
  
  private var tmp_color = Color.WHITE
  
  def getColor(background_color:Color,paint_color:Color,opacity:Double):Color={
    val res = Colors.smudge(tmp_color, background_color, paint_color, opacity)
    tmp_color = background_color
    return res
  }
  def setColor(background_color:Color){
    tmp_color = background_color
  }

}