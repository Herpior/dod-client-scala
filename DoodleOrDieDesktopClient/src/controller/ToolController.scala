package controller

import view.ToolPanel
import scala.swing.Reactor
import swing.event._
import swing.Dialog
import dmodel.Coord
import dmodel.Magic

class ToolController(tools:ToolPanel)extends Reactor{
  listenTo(tools.mouse.clicks)
  listenTo(tools.mouse.moves)
  val model = tools.model
  
      this.reactions += {
        case e:MouseDragged =>
          val x = e.point.getX-25
          val y = e.point.getY
          if(model.changingSize){
            model.setSize(x.toInt)
            tools.publish(new SizeChangeEvent(model.getSize))
            //this.nextsize = min(max(x.toInt,1),200)
            tools.repaint
          }
          /*if(mini){
              val xx = magicX-(x*magicX/200).toInt
              val yy = magicY-((y-620)*magicY/150).toInt
              this.setPoint(xx, yy)
              this.publish(new ZoomEvent)
              repaint
          }*/
        case e:MousePressed =>
          val x = e.point.getX-25
          val y = e.point.getY
          //println(e.modifiers)
          if(y>110 && y<140){
            model.setSize(x.toInt)
            model.pressSlider
            tools.publish(new SizeChangeEvent(model.getSize))
            tools.repaint
          }  /*else if(y>620&&y<770){
              mini = true
              val xx = magicX-(x*magicX/200).toInt
              val yy = magicY-((y-620)*magicY/150).toInt
              this.setPoint(xx, yy)
              this.publish(new ZoomEvent)
              repaint
            }*/
        case e:MouseReleased => 
          val x = e.point.getX.toInt-25
          val y = e.point.getY
          //println(y)
          if(model.changingSize){
            model.setSize(x.toInt)
            model.releaseSlider
            //tools.publish(new SizeChangeEvent(model.getSize))
            tools.repaint()
          }/*else if(mini){
            mini = false
          }*/
          else {
            if(y<50){
              x/50 match {
                case 0 => model.setSize(1)
                case 1 => model.setSize(3)
                case 2 => model.setSize(5)
                case _ => model.setSize(10)
              }
              //tools.publish(new SizeChangeEvent(model.getSize))
            }else if(y<110){
              x/50 match {
                case 0 => model.setSize(25)
                case 1 => model.setSize(50)
                case 2 => model.setSize(100)
                case _ => if(Magic.authorized)model.setSize(200)
              }
              //tools.publish(new SizeChangeEvent(model.getSize))
            }else if(y<140){
              model.setSize(x.toInt)
              model.releaseSlider
            //tools.publish(new SizeChangeEvent(model.getSize))
            }else if(y<250){
              if(x> -20 && x<205){
                //println(e.modifiers+" -> "+e.modifiers/4096%2)
                if(e.modifiers/256%2==1){
                  model.secondaryColor(Coord((x+18),(y-147)),Coord(230,106))
                  //println("secondary")
                    //tools.publish(new ColorChangeEvent(model.getColor(1),1))
                } else {
                  model.primaryColor( Coord((x+18),(y-147)),Coord(230,106))
                    //tools.publish(new ColorChangeEvent(model.getColor(0),0))
                
                  if(e.modifiers/128%2==1 && Magic.authorized){
                    val dres = Dialog.showInput(tools, "Set a new color", "color", Dialog.Message.Question, null, Nil, model.getColor(0))
                    dres.foreach(col => model.setColor(col))
                  }
                }
              }
            }else if(y<400){
              
            }else if(y<500){
              if(x>0&&x<200){
                tools.model.tool( x/50+(y.toInt-400)/50*4 )
                tools.publish(new ToolChangeEvent(tools.model.getState))
              }
            }else if(y<600){
              
            }else if(y<650){
              if(x<200&&x>0)
              tools.publish(new SubmitEvent)
            }
            /*else if(y<770){
              mini = false
              val xx = magicX-(x*magicX/200).toInt
              val yy = magicY-((y-620)*magicY/150).toInt
              this.publish(new ZoomEvent)
              this.setPoint(xx, yy)
            }*/
        }
        tools.repaint
      }
}