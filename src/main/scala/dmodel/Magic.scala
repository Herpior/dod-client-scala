package dmodel

import java.awt.Color

object Magic {

  def authorized = http.HttpHandler.getAuth || offline
  val faster = false // if true will make semitransparent lines faster but look worse when drawing
  val fasterPan = true //if true will move only the images when dragging, rather than drawing everything again every pixel
  val readyDefault = false //default for ready checkbox
  var x = 520
  var y = 390
  val rows = 16
  val thumbX = 60//100
  val thumbY = 45//75
  val thumbZoom = thumbX.toDouble/x
  var doodleSize = new Coord(x,y)
  val bgColor = Color.decode("#b7e4f4")
  val bgColorAlpha = new Color(183,228,244,192)
  val buttColor = Color.decode("#4cb9dd")
  val white = Color.white
  val red = Color.decode("#b94cdd")
  val black = Color.decode("#111111")
  val maxChars = 280
  val hexa = "0123456789abcdef"
  val font20 = new swing.Label(" ").font.deriveFont(java.awt.Font.BOLD,20)
  var offline = false
  var user = ""
  val roundingAccuracy = 10 //determines how accurately the lines are drawn, saved and uploaded. 2 => 0, 0.5, 1, ... 10 => 0, 0.1, 0.2, ...
  
  def setXY(nx:Int, ny:Int) {
    x = nx
    y = ny
    doodleSize = Coord(x, y)
  }
}