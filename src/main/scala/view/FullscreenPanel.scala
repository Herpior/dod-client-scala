package view

import scala.swing._
import javax.swing.JFrame
import java.awt.{GraphicsDevice, GraphicsEnvironment}

class FullscreenPanel(past:DoodlingPanel) extends JFrame {
  
  this.add(past.peer); //TODO make fullscreen work, no idea what this will do at the moment
  
  val gd: GraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice

  if (gd.isFullScreenSupported) {
      setUndecorated(true)
    gd.setFullScreenWindow(this)
  } else {
      System.err.println("Full screen not supported")
    setSize(100, 100); // just something to let you see the window
      setVisible(true)
  }
  
}