package controller
import javax.swing.Timer

object Timer {
  def apply(interval: Int, repeats: Boolean = true)(op: => Unit): Timer ={
    val timeOut = new javax.swing.AbstractAction() {
      def actionPerformed(e : java.awt.event.ActionEvent): Unit = op
    }
    val t = new javax.swing.Timer(interval, timeOut)
    t.setRepeats(repeats)
    t
  }
}