package dmodel

/**
  * A class that mixes colours between the brush colour and the background colour but it doesn't work very well
  * and it is not currently used in the application.
  *

  * @author Qazhax
  */

import java.awt.Color

class Smudge {
  
  private var tmp_color = Color.WHITE
  
  def getColor(background_color:Color,paint_color:Color,opacity:Double):Color={
    val res = Colors.smudge(tmp_color, background_color, paint_color, opacity)
    tmp_color = background_color
    res
  }
  def setColor(background_color:Color){
    tmp_color = background_color
  }

}