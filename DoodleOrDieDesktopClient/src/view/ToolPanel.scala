package view

import scala.swing.BoxPanel
import scala.swing.Reactor
import swing.event._
import swing.Dialog
import dmodel.Coord
import dmodel.Magic
import dmodel.Colors
import scala.swing.Dimension
import scala.swing.Orientation
import java.awt.Font
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic
import dmodel.ToolModel
import io.Icons
import javafx.scene.control.ColorPicker

class ToolPanel/*(model:ToolModel)*/ extends BoxPanel(Orientation.Vertical){
  val model = ToolModel
  model.initReady
  this.minimumSize = new Dimension(250,450)
  this.preferredSize = this.minimumSize
  this.minimumSize = new Dimension(300,450)
  
  def isReady = this.model.isReady
  
  this.contents += new SizePanel
  this.contents += new ColorPanel
  this.contents += new ToolPickerPanel
  
  //val model = new ToolModel
  
  //this.visible = true
  //this.revalidate()
  //private var magicX = 520
  //private var magicY = 390
  //private var colorpicker = 0
  //private var colorpicker2 = 0
  //private var zoom = 2.0
  //private var state = 0
  //private var thumbs = Array[BufferedImage]()
  
  
  
      //def zoomval = zoom
      //def bstate = state
      
      
      //val sizes = Vector(1,3,5,10,25,50,100,200)
      //this.contents += new Panel{this.preferredSize = new Dimension(300,780)}
  /*def addThumb(index:Int) = {
        this.thumbs = this.thumbs.take(index)++
    Array(new BufferedImage(200,150,BufferedImage.TYPE_INT_ARGB))++
        this.thumbs.drop(index)
  }*/
      
      /*def zoomin(num:Int) = {
        val changed = (zoom*(math.pow(2,-num))*10).toInt/10.0
        if(changed>=0.5 && changed<=32) zoom = changed
        val z = (-1/(zoom/2)+1)
          offX = (260*z).toInt //0.5->-2, 1-> -1, 2->0, 4->0.5, 8->0.75
          offY = (195*z).toInt // -1/z+1
        this.publish(new ZoomEvent)
        repaint
      }*/
      /*def setPoint(x:Int,y:Int){
        pointX = min(max(x,0),magicX)
        pointY = min(max(y,0),magicY)
        this.publish(new ZoomEvent)
        repaint
      }*/
      /*def move(dirx:Double,diry:Double) = {
        pointX = min(max(pointX-dirx,0),magicX)
        pointY = min(max(pointY-diry,0),magicY)
        this.publish(new ZoomEvent)
        repaint
      }*/
      /*def setthumb(layer:Int,img:BufferedImage){
        thumbs(layer) = img
      }*/
      override def paintComponent(g:Graphics2D){
    var offy = 5
      //showPicker
        this.background = Magic.bgColor
        super.paintComponent(g)
        //g.setColor(bgc)
        //g.fillRect(0, 0, size.getWidth.toInt, size.getHeight.toInt)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
        //println("why")
        offy += 105
        
        offy += 40
            
        offy += 130
        
        offy += 100
        
        g.setColor(Magic.buttColor)
        g.fillRoundRect(25, offy, 200, 50, 15, 15)
        g.setColor(Magic.white)
        g.setFont(Magic.font20)
        g.drawString("Submit!", 90, offy+35)
        
        offy += 100
        
        g.drawRoundRect(25, offy, 30, 30, 4, 4)
        if(isReady) g.drawImage(Icons.getCheck,30,offy+5,null)
        g.setFont(Magic.font20.deriveFont(java.awt.Font.BOLD,18))
        g.drawString("Ready to submit", 70, offy+20)
      }
      
      
      

//class ToolController(tools:ToolPanel)extends Reactor{
  listenTo(mouse.clicks)
  listenTo(mouse.moves)
  //val model = model
  
      this.reactions += {
        case e:MouseDragged =>
          val x = e.point.getX-25
          val y = e.point.getY
          if(model.changingSize){
            model.setSize(x.toInt)
            publish(new controller.SizeChangeEvent(model.getSize))
            //this.nextsize = min(max(x.toInt,1),200)
            repaint
          }
          /*if(mini){
              val xx = magicX-(x*magicX/200).toInt
              val yy = magicY-((y-620)*magicY/150).toInt
              this.setPoint(xx, yy)
              this.publish(new ZoomEvent)
              repaint
          }*/
        case e:MouseClicked =>
          val y = e.point.getY
          //println(e.modifiers+" mods toolpanel")
          if(e.clicks>1 && y>148&&y<269 && e.modifiers == 0 && Magic.authorized){
                    val dres = swing.ColorChooser.showDialog(this,"pick a color",model.getColor)//(0))//Dialog.showInput(this, "Set a new color", "color", Dialog.Message.Question, null, Nil, Colors.toHexString(model.getColor(0)))
                    dres.foreach(col => model.setColor(col))
                    this.repaint()
                  }
        case e:MousePressed =>
          val x = e.point.getX-25
          val y = e.point.getY
          //println(e.modifiers)
          if(y>110 && y<140){
            model.setSize(x.toInt)
            model.pressSlider
            publish(new controller.SizeChangeEvent(model.getSize))
            repaint
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
            //publish(new SizeChangeEvent(model.getSize))
            repaint()
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
              //publish(new SizeChangeEvent(model.getSize))
            }else if(y<110){
              x/50 match {
                case 0 => model.setSize(25)
                case 1 => model.setSize(50)
                case 2 => model.setSize(100)
                case _ => if(Magic.authorized)model.setSize(200)
              }
              //publish(new SizeChangeEvent(model.getSize))
            }else if(y<140){
              model.setSize(x.toInt)
              model.releaseSlider
            //publish(new SizeChangeEvent(model.getSize))
            }else if(y<148){
              
            }
            else if(y<269){
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
              
            }else if(y<375){
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
        }
        repaint
      }
//}
      
      
  }