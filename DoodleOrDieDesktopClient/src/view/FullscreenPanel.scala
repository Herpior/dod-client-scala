package view

import scala.swing._

class FullscreenPanel(past:DoodlingPanel) extends BoxPanel(Orientation.NoOrientation) with WindowPanel{
  this.contents += past.doodle
  
}