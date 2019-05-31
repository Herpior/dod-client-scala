package dmodel

/**
  * An object that stores the config values and magic numbers used all over the application.
  *

  * @author Qazhax
  */

import java.awt.{Color, Font}

object Magic {
//TODO: move io.localstorage.loadConfig into the main window?
  private val loadedConf = try { io.LocalStorage.loadConfig("config") } catch { case e:Throwable => Map[String, String]()}
  def authorized: Boolean = true//http.HttpHandler.getAuth || offline
  val version = 501
  val faster = false // if true will make semitransparent lines faster but look worse when drawing
  val fasterPan: Boolean = try {loadedConf.getOrElse("fasterPan","true").toBoolean} catch {case e:Throwable => true} //if true will move only the images when dragging, rather than drawing everything again every pixel
  val readyDefault: Boolean = try {loadedConf.getOrElse("readyDefault","false").toBoolean} catch {case e:Throwable => false} //default for ready checkbox
  var x = 520
  var y = 390
  val rows = 16
  val thumbX = 60//100
  val thumbY = 45//75
  val thumbZoom: Double = thumbX.toDouble/x
  var doodleSize = new Coord(x,y)
  val bgColor: Color = Color.decode("#b7e4f4")
  val bgColorAlpha = new Color(183,228,244,192)
  val buttColor: Color = Color.decode("#4cb9dd")
  val darkerColor: Color = Color.decode("#2e6287")
  val white: Color = Color.white
  val red: Color = Color.decode("#b94cdd")
  val black: Color = Color.decode("#111111")
  val maxChars = 280
  val hexa = "0123456789abcdef"
  val font20: Font = new swing.Label(" ").font.deriveFont(java.awt.Font.BOLD,20)
  var offline = false
  var user = ""
  var roundingAccuracy:Int = try {math.max(1,math.min(10, loadedConf.getOrElse("accuracy","2").toInt))} catch {case e:Throwable => 2} //determines how accurately the lines are drawn, saved and uploaded. 2 => 0, 0.5, 1, ... 10 => 0, 0.1, 0.2, ...
  val namira: Boolean = try {loadedConf.getOrElse("namira","false").toBoolean} catch {case e:Throwable => false} //changes skincolor to beige

  def setXY(nx:Int, ny:Int) {
    x = nx
    y = ny
    doodleSize = Coord(x, y)
  }
}