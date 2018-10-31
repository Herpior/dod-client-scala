package controller

import scala.swing.event._

class SizeChangeEvent(val size:Int) extends Event {

}

class ColorChangeEvent(val color:String,val index:Int) extends Event{
  
}

class ToolChangeEvent(val tool:Int) extends Event{
  
}

class SubmitEvent extends Event