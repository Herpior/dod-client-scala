package view

import scala.swing.Panel

//import view.ToolPanel
import scala.swing.Reactor
import swing.event._
import swing.Dialog
import dmodel.Coord
import dmodel.Magic
import dmodel.Colors
import scala.swing.Dimension
import java.awt.Font
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import dmodel.Magic
import dmodel.ToolModel
import io.Icons
import javafx.scene.control.ColorPicker

class ToolPanel/*(model:ToolModel)*/ extends Panel{
  val model = ToolModel
  this.minimumSize = new Dimension(250,450)
  this.preferredSize = this.minimumSize
  this.minimumSize = new Dimension(300,450)
  
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
        g.setColor(Magic.buttColor)
        for(y<-0 to 1){
          for(x<-0 to 3){
            g.fillOval(x*50+30, y*50+offy, 40, 40)
          }
        }
        
        g.setColor(Color.WHITE)
        g.setFont(g.getFont.deriveFont(Font.BOLD))
        g.drawString("XXS", 14+25, 29)
        g.drawString("XS", 68+25, 29)
        g.drawString("S", 122+25, 29)
        g.drawString("M", 171+25, 29)
        g.drawString("L", 22+25, 79)
        g.drawString("XL", 69+25, 79)
        g.drawString("XXL", 116+25, 79)
        g.drawString("XXXL", 160+25, 79)
        val sizein = model.sizes.indexOf(model.getSize)
        if(sizein > -1){
          val x = sizein % 4
          val y = sizein / 4
          g.setColor(Color.WHITE)
          g.fillOval(x*50+30, y*50+offy, 40, 40)
          g.setColor(Magic.buttColor)
          val b = 24+offy
          val t = 50+b
          sizein match{
            case 0 => g.drawString("XXS", 14+25, b)
            case 1 => g.drawString("XS", 68+25, b)
            case 2 => g.drawString("S", 122+25, b)
            case 3 => g.drawString("M", 171+25, b)
            case 4 => g.drawString("L", 22+25, t)
            case 5 => g.drawString("XL", 69+25, t)
            case 6 => g.drawString("XXL", 116+25, t)
            case 7 => g.drawString("XXXL", 160+25, t)
          } 
        } 
        offy+=105
        val size = this.model.getSize
        g.setColor(Magic.buttColor)
        g.fillRect(25, offy+12, 200, 6)
        //g.fillOval(155, 155, 40, 40)
        g.setColor(Color.WHITE)
        g.fillOval(size.toInt+10, offy, 30, 30)
        g.setColor(Magic.buttColor)
        val offset = size.toInt.toString().length()*3
        g.drawString(""+size, size+25-offset, offy+19)
        
          val wid = 230/model.rowl
          val hei = 120/model.rows
          val inter = hei/20
          val inter2 = inter/2
          val colors = model.getColors
          offy += 40
          var currx = 0
          var curry = 0
          var old = Magic.white
        for(i<-colors.indices){
          g.setColor(colors(i))
          val x = 10+(i%model.rowl)*wid+inter2
          val y = offy+(i/model.rowl)*hei+inter2
          g.fillRoundRect(x, y, wid-inter, hei-inter, wid/4, wid/4)
          if(i == model.colorIndex){
            currx = x
            curry = y
            old = g.getColor
          }
        }
            val c = Colors.inverse(old)
            val c2 = if(old.getRed+old.getBlue+old.getGreen>255*1.5) Color.BLACK else Color.WHITE
            g.setColor(c2)
            g.setStroke(new BasicStroke(1))
            g.drawRoundRect(currx+1, curry+1, wid-inter-2, hei-inter-2, wid/4, wid/4)
            g.setColor(c)
            g.drawRoundRect(currx, curry, wid-inter, hei-inter, wid/4, wid/4)
            
        offy += 130
            
        g.setColor(Magic.buttColor)
        g.setStroke(new BasicStroke(2))
        for(i<-0 to 7){
          g.fillRoundRect(28+i*200/4-i/4*200, offy+i/4*50, 44, 44, 4, 4)
        }
        g.setColor(Magic.white)
        val state = model.getState
        //println("srtat: "+state)
        g.drawRoundRect(28+state*200/4-state/4*200, offy+state/4*50, 44, 44, 4, 4)
        g.drawImage(Icons.getPen,24,offy-3,null)
        g.drawImage(Icons.getLine,75,offy-2,null)
        g.drawImage(Icons.getBez,125,offy-2,null)
        g.drawImage(Icons.getFill,175,offy-2,null)
        g.drawImage(Icons.getPers,25,offy+48,null)
        g.drawImage(Icons.getBezFill,125,offy+48,null)
        g.setColor(Magic.buttColor)
        
        offy += 100
        
        g.fillRoundRect(25, offy, 200, 50, 15, 15)
        g.setColor(Magic.white)
        g.setFont(Magic.font20)
        g.drawString("Submit!", 90, offy+35)
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
            /*else if(y<770){
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