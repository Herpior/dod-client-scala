package controller

import view.WindowPanel

import scala.swing.event._

class ReplaceEvent(val replacement:WindowPanel, val source:Any) extends Event{

}
//class InternalReplaceEvent(val replacement:BoxPanel)extends Event{
//  
//}
class FullscreenEvent extends Event

class RepaintEvent extends Event

class ZoomDoodleEvent(val direction:Int) extends RepaintEvent

class RepaintDoodleEvent(val bottom:Boolean = false,
                         val current:Boolean = false,
                         val last_curr:Boolean = false,
                         val drawing:Boolean = false,
                         val last_draw:Boolean = false,
                         val top:Boolean = false) extends RepaintEvent


class SizeChangeEvent(val size:Int) extends Event

class ColorChangeEvent(val color:String, val index:Int) extends Event

class ToolChangeEvent(val tool:Int) extends Event

class SubmitEvent extends Event