package dmodel

import java.awt.image.BufferedImage
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.BasicStroke

import dmodel.dpart.{BasicLine, DoodlePart}

object LineDrawer {

  def paintPercentage(layers:Array[Layer]) = {
    val img = new java.awt.image.BufferedImage(200,150,java.awt.image.BufferedImage.TYPE_INT_ARGB)
    val col = img.getRGB(0,0)
    val alp = (col>>24)&0xff
    //println("alpha: "+alp)
    val g = img.createGraphics
    layers.foreach{
      l=>l.getVisibleStrokes(true).foreach { x =>
        LineDrawer.drawDoodlePart(g, x, 200.0/Magic.x, Coord(0,0), true)
      }
    }
    var paint = 0
    for(i<-0 until 200){
      for(j<-0 until 150){
        val alpha = (img.getRGB(i, j)>>24)&0xff
        //println(alpha)
        if(alpha !=alp){
          paint += 1
        }
      }
    }
    paint/300
  }
  
  def thumb(layer:Layer)={
    val img = new BufferedImage(Magic.thumbX,Magic.thumbY,BufferedImage.TYPE_INT_ARGB)
    val g = img.createGraphics()
    g.setColor(Magic.buttColor)
    g.fillRect(0, 0, Magic.thumbX, Magic.thumbY)
    layer.getStrokes.foreach{
      dp=> drawDoodlePart(g,dp,Magic.thumbZoom,Coord(0,0),true)
      //println(dp)
    }
    img
  }
  
  def redraw2(img:BufferedImage,strokes:Array[DoodlePart]){
    val g = img.createGraphics()
    //println(cur.strokes.mkString(", "))
    for(dp<-strokes){
      //var last = -1
      drawDoodlePart(g,dp,2,new Coord(0,0),false)
    }
  }
  def drawDoodlePartLast(g:Graphics2D,dp:DoodlePart,czoom:Double,offs:Coord,antialiasing:Boolean){
    try {
      val last = dp.getLines.last
      val coords = last.getCoords.takeRight(2)
      val line = new BasicLine(last.color,last.size){this.setCoords(coords)}
      drawDoodlePart(g, line, czoom, offs, antialiasing)
    }
    catch {
      case e:Throwable => e.printStackTrace
    }
  }
  def drawDoodlePart(g:Graphics2D,dp:DoodlePart,czoom:Double,offs:Coord,antialiasing:Boolean){
    if(antialiasing)g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON)
    else g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)
    for(st<-dp.getLines){
        g.setStroke(new BasicStroke((st.size*czoom).toFloat,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND))
        g.setColor(st.color)
        //if(last != -1)
        //val path = st.path.map(x=>math.round(x*2)/2.0*czoom)//(_*czoom)
        //val len2 = path.length/2
        val off = offs//*czoom//if(czoom<=2) Coord(0,0) else (offs*czoom)
        //val ox = if(czoom<2) 0 else (offsetX*czoom)
        //val oy = if(czoom<2) 0 else (offsetY*czoom)
        val coords = st.getCoords.map(c=>c.rounded(Magic.roundingAccuracy)*czoom+off)
        //if(!antialiasing)println(czoom)//(coords.mkString(", "))
        //val xs = st.xs.map(x=>(math.round(x*2)/2.0*czoom-ox).toInt)
        //val ys = st.ys.map(y=>(math.round(y*2)/2.0*czoom-oy).toInt)
        //println(xs.mkString(", "))
        //if(len2==1) g.drawLine((path(0)-ox).toInt, (path(1)-oy).toInt, (path(0)-ox).toInt, (path(1)-oy).toInt)
        //else for(index<-0 until len2-1){
          g.drawPolyline(coords.map(_.x.toInt), coords.map(_.y.toInt), coords.length)  
          //g.drawLine((path(2*index)-ox).toInt, (path(2*index+1)-oy).toInt, (path(2*index+2)-ox).toInt, (path(2*index+3)-oy).toInt)
          //}
      }
  }
}