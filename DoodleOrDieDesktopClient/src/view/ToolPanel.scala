package view

import scala.swing.BoxPanel
import scala.swing.Dialog
import scala.swing.Dimension
import scala.swing.event._
import scala.swing.Orientation
//import scala.swing.Reactor
import dmodel.Magic
import dmodel.ToolModel

class ToolPanel extends BoxPanel(Orientation.Vertical){
  
  val model = ToolModel
  
  this.minimumSize = new Dimension(200,450)
  this.preferredSize = new Dimension(250,450)
  this.maximumSize = new Dimension(300,4500)
  this.background = Magic.bgColor
  
  this.contents += new SizePanel
  this.contents += new ColorPanel
  this.contents += new ToolPickerPanel
  this.contents += new SubmitPanel
      

//class ToolController(tools:ToolPanel)extends Reactor{
  listenTo(mouse.clicks)
  listenTo(mouse.moves)
  //val model = model
  
      this.reactions += {
          /*else if(y>620&&y<770){
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
          /*if(model.changingSize){
            //model.setSize(x.toInt)
            model.releaseSlider
            //publish(new SizeChangeEvent(model.getSize))
            //repaint()
          }/*else if(mini){
            mini = false
          }*/
          else {*/
            /*if(y<140){
              model.setSize(x.toInt)
              model.releaseSlider
            //publish(new SizeChangeEvent(model.getSize))
            }else if(y<148){
              
            }
            else */ /*if(y<269){
              if(x> -20 && x<205){
                //println(e.modifiers+" -> "+e.modifiers/4096%2)
                if(e.modifiers/256%2==1){
                  model.secondaryColor(Coord((x+18),(y-147)),Coord(230,120))
                  //println("secondary")
                    //publish(new ColorChangeEvent(model.getColor(1),1))
                } else {
                  model.primaryColor( Coord((x+18),(y-147)),Coord(230,120))
                    //publish(new ColorChangeEvent(model.getColor(0),0))
                
                  if(e.modifiers/128%2==1 && Magic.authorized){
                    val dres = swing.ColorChooser.showDialog(this,"pick a color",model.getColor)//(0))//Dialog.showInput(this, "Set a new color", "color", Dialog.Message.Question, null, Nil, Colors.toHexString(model.getColor(0)))
                    dres.foreach(col => model.setColor(col))
                    this.repaint()
                  }
                }
              }
            }else if(y<275){
              
            }else */if(y<375){
              val x1 = x+3
              if(x1>0&&x1<200){
                model.tool( x1/50+(y.toInt-275)/50*4 )
                publish(new controller.ToolChangeEvent(model.getState))
              }
            }else if(y<430){
              if(x<200&&x>0)
              //println("submit "+y)
                publish(new controller.SubmitEvent)
            }
            else if(y<480){
              if(x<200&&x>0)
              println("submit "+y)
            }
            else if(y<520){
              if(x<200&&x>0){
                model.clickReady
                this.repaint()
              }
            }/*
            else if(y<530){
              mini = false
              val xx = magicX-(x*magicX/200).toInt
              val yy = magicY-((y-620)*magicY/150).toInt
              this.publish(new ZoomEvent)
              this.setPoint(xx, yy)
            }*/
        //}
        repaint
      }
//}
      
      
  }