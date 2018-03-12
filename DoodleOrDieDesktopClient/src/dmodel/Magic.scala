package dmodel

import java.awt.Color

object Magic {

  def authorized = http.HttpHandler.getAuth
  val faster = false // if true will make semitransparent lines faster but look worse when drawing
  val fasterPan = true //if true will move only the images when dragging, rather than drawing everything again every pixel
  val readyDefault = false //default for ready checkbox
  val x = 520
  val y = 390
  val rows = 16
  val thumbX = 60//100
  val thumbY = 45//75
  val thumbZoom = thumbX.toDouble/x
  val doodleSize = new Coord(x,y)
  val bgColor = Color.decode("#b7e4f4")
  val bgColorAlpha = new Color(183,228,244,192)
  val buttColor = Color.decode("#4cb9dd")
  val white = Color.white
  val red = Color.decode("#b94cdd")
  val black = Color.decode("#111111")
  val maxChars = 140
  val hexa = "0123456789abcdef"
  val font20 = new swing.Label(" ").font.deriveFont(java.awt.Font.BOLD,20)
  
}