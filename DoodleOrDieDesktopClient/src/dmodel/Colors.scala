package dmodel

import java.awt.Color
import collection.mutable.Buffer

object Colors {

  def toHexString(color:Color)={
    var r = Integer.toHexString(color.getRed())
    var g = Integer.toHexString(color.getGreen())
    var b = Integer.toHexString(color.getBlue())
      if(r.length()<2)r="0"+r
      if(g.length()<2)g="0"+g
      if(b.length()<2)b="0"+b
      if(r(0)==r(1)&&g(0)==g(1)&&b(0)==b(1)){
        r=r(0)+""
        g=g(0)+""
        b=b(0)+""
      }
      
    "#"+r+""+g+""+b
  }
  /*def toHexString(color:javafx.scene.paint.Color)={
    var r = Integer.toHexString((color.getRed()*255).toInt)
    var g = Integer.toHexString((color.getGreen()*255).toInt)
    var b = Integer.toHexString((color.getBlue()*255).toInt)
      if(r.length()<2)r="0"+r
      if(g.length()<2)g="0"+g
      if(b.length()<2)b="0"+b
    "#"+r+""+g+""+b
  }*/
  def toColor(string:String)={
    var str = string.toLowerCase
    if(string.headOption.exists { _=='#' }){
      str = str.tail
    }
    val hexa = str.forall(c=>Magic.hexa.exists { x => x==c })
    /*if(hexa && str.length()==6){
      val r=Integer.parseInt(str.take(2),16)
      val g=Integer.parseInt(str.drop(2).take(2),16)
      val b=Integer.parseInt(str.drop(4),16)
      new Color(r, g, b)
    } else*/ if(hexa && str.length==3){
      val r=Integer.parseInt(str(0)+""+str(0),16)
      val g=Integer.parseInt(str(1)+""+str(1),16)
      val b=Integer.parseInt(str(2)+""+str(2),16)
      new Color(r, g, b)
    } else Color.decode(string)
  }/*
  def toColor(awtColor:Color)={
    val r = awtColor.getRed
    val g = awtColor.getGreen
    val b = awtColor.getBlue
    javafx.scene.paint.Color.rgb(r, g, b)
  }
  def toColor(paintColor:javafx.scene.paint.Color)={
    val r = (paintColor.getRed*255).toInt
    val g = (paintColor.getGreen*255).toInt
    val b = (paintColor.getBlue*255).toInt
    new Color(r, g, b)
  }*/
  
  def inverse(colorstr:String):String={
    toHexString( inverse(toColor(colorstr)) )
  }
  def inverse(color:Color):Color={
    val r = 255-color.getRed()
    val g = 255-color.getGreen()
    val b = 255-color.getBlue()
    new Color(r,g,b)
    /*var r = Integer.toHexString(255-color.getRed())
    var g = Integer.toHexString(255-color.getGreen())
    var b = Integer.toHexString(255-color.getBlue())
      if(r.length()<2)r="0"+r
      if(g.length()<2)g="0"+g
      if(b.length()<2)b="0"+b
    "#"+r+""+g+""+b*/
  }
  def linearcolor(n:Int,rgb:Boolean,fcolor:Color,bcolor:Color) :Array[Color] = {
    val first = fcolor//Color.decode(fcolor)
    val last =  bcolor//Color.decode(bcolor)
    if(first == last) return Array.fill(n)(bcolor)
    val buf = Buffer[Color]()
    val fr = first.getRed//RGB
    val fg = first.getGreen
    val fb = first.getBlue
    val lr = last.getRed
    val lg = last.getGreen
    val lb = last.getBlue
    val fhsb = Color.RGBtoHSB(fr, fg, fb, null)
    val lhsb = Color.RGBtoHSB(lr, lg, lb, null)
    val hf = if(fr==fg && fg==fb) lhsb(0) else fhsb(0)
    val sf = fhsb(1)
    val bf = fhsb(2)
    val hl = if(lr==lg && lg==lb) hf else lhsb(0)
    val sl = lhsb(1)
    val bl = lhsb(2)
    var dh = hl - hf
    if(math.abs(dh)>0.5){
      dh = if(dh>0)-1+dh else 1+dh
    }
    if(!rgb)
    for(i<-0 until n){
      val coeff = i*1.0/(n-1)
      val negef = 1-coeff
      var hh = (hf+dh*negef).toFloat
      while(hh>1)hh-=1
      while(hh<0)hh+=1
      val ss = (sf*coeff+sl*negef).toFloat
      val bb = (bf*coeff+bl*negef).toFloat
      val colr = Color.getHSBColor(hh, ss, bb)
      /*var r = Integer.toHexString(colr.getRed())
      var g = Integer.toHexString(colr.getGreen())
      var b = Integer.toHexString(colr.getBlue())
      if(r.length()<2)r="0"+r
      if(g.length()<2)g="0"+g
      if(b.length()<2)b="0"+b
      buf += "#"+r+""+g+""+b*/
      buf += colr
    }
    else for(i<-0 until n){
      val coeff = i*1.0/(n-1)
      //print(coeff+" -> ")
      val negef = 1-coeff
      val r = (fr*coeff+lr*negef).toInt
      val g = (fg*coeff+lg*negef).toInt
      val b = (fb*coeff+lb*negef).toInt
      buf += new Color(r,g,b)
      /*var r = Integer.toHexString(math.round(fr*coeff+lr*negef).toInt)
      var g = Integer.toHexString(math.round(fg*coeff+lg*negef).toInt)
      var b = Integer.toHexString(math.round(fb*coeff+lb*negef).toInt)
      if(r.length()<2)r="0"+r
      if(g.length()<2)g="0"+g
      if(b.length()<2)b="0"+b
      buf += "#"+r+""+g+""+b*/
      //print(r+g+b+" -> ")
    }
    buf.toArray
  }
  
}