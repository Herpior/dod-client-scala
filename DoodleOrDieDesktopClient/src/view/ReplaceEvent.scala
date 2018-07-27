package view

import scala.swing.event._
import scala.swing._

class ReplaceEvent(val replacement:WindowPanel, val source:Any) extends Event{

}
//class InternalReplaceEvent(val replacement:BoxPanel)extends Event{
//  
//}
class FullscreenEvent extends Event

class RepaintEvent extends Event