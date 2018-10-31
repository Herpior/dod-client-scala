package dmodel

import java.awt.Color
import collection.mutable.Buffer

object Colors {

  def toHexString(color:Color):String={
    if(color.getAlpha!=255)return toRGBAString(color)
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
  def toHexRGBA(color:Color):String={
    val alpha = color.getAlpha()
    var r = Integer.toHexString(color.getRed())
    var g = Integer.toHexString(color.getGreen())
    var b = Integer.toHexString(color.getBlue())
    var a = Integer.toHexString(alpha)
      if(r.length()<2)r="0"+r
      if(g.length()<2)g="0"+g
      if(b.length()<2)b="0"+b
      if(a.length()<2)a="0"+a
      if(r(0)==r(1)&&g(0)==g(1)&&b(0)==b(1)&&alpha==255){
        r=r(0)+""
        g=g(0)+""
        b=b(0)+""
      }
      
    if(alpha==255)"#"+r+""+g+""+b
    else "#"+r+""+g+""+b+""+a
  }
  def toRGBAString(color:Color)={
    "rgba("+color.getRed()+","+color.getGreen()+","+color.getBlue()+","+(color.getAlpha()*1000/255.0).round/1000.0+")"
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
  def toColor(string:String):Color = {
    if(string==null){return Color.black}
    var str = string.toLowerCase
    //println(string.take(4))
    if(string.headOption.exists { _=='#' }){
      str = str.tail
    } else if(str.take(4)=="rgba"){
      val txt = str.drop(5).dropRight(1)
      //println(str+" = "+string)
      val parts = txt.split(",")
      if(parts.size!=4)return Color.BLACK//println("fails")
      return new Color(parts(0).toInt,parts(1).toInt,parts(2).toInt,(parts(3).toDouble*255).toInt)
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
    } else if(hexa && str.length==8){
      val r=Integer.parseInt(str(0)+""+str(1),16)
      val g=Integer.parseInt(str(2)+""+str(3),16)
      val b=Integer.parseInt(str(4)+""+str(5),16)
      val a=Integer.parseInt(str(6)+""+str(7),16)
      new Color(r, g, b, a)
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
  def smudge(first:Color,second:Color, add:Color=Color.WHITE, opacity:Double=0) :Color = {
    val red = ((first.getRed+second.getRed)*0.5*(1-opacity)+opacity*add.getRed).toInt
    val green = ((first.getGreen+second.getGreen)*0.5*(1-opacity)+opacity*add.getGreen).toInt
    val blue = ((first.getBlue+second.getBlue)*0.5*(1-opacity)+opacity*add.getBlue).toInt
    
    return new Color(red,green,blue)
  }
  def linearcolor(n:Int,rgb:Boolean,fcolor:Color,bcolor:Color) :Array[Color] = { 
    val first = fcolor//Color.decode(fcolor)
    val last =  bcolor//Color.decode(bcolor)
    if(first == last) return Array.fill(n)(bcolor)
    val buf = Buffer[Color]()
    val fr = first.getRed//RGB
    val fg = first.getGreen
    val fb = first.getBlue
    val fa = first.getAlpha
    val lr = last.getRed
    val lg = last.getGreen
    val lb = last.getBlue
    val la = last.getAlpha
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
    if(!rgb) for(i<-0 until n){
        val coeff = i*1.0/(n-1)
        val negef = 1-coeff
        var hh = (hf+dh*negef).toFloat
        while(hh>1)hh-=1
        while(hh<0)hh+=1
        val ss = (sf*coeff+sl*negef).toFloat
        val bb = (bf*coeff+bl*negef).toFloat
        val aa = (fa*coeff+la*negef).toInt
        val colr = Color.getHSBColor(hh, ss, bb)
        /*var r = Integer.toHexString(colr.getRed())
        var g = Integer.toHexString(colr.getGreen())
        var b = Integer.toHexString(colr.getBlue())
        if(r.length()<2)r="0"+r
        if(g.length()<2)g="0"+g
        if(b.length()<2)b="0"+b
        buf += "#"+r+""+g+""+b*/
        buf += new Color(colr.getRed,colr.getGreen,colr.getBlue, aa)
      }
    else {
      val gamma = 2.0 //TODO: move to parameters
      val negamma = 1/gamma
      val frg = math.pow(fr,gamma)
      val fgg = math.pow(fg,gamma)
      val fbg = math.pow(fb,gamma)
      val lrg = math.pow(lr,gamma)
      val lgg = math.pow(lg,gamma)
      val lbg = math.pow(lb,gamma)
      
      for(i<-0 until n){
        val coeff = i*1.0/(n-1)
        //print(coeff+" -> ")
        val negef = 1-coeff
        val r = math.pow(frg*coeff+lrg*negef, negamma).toInt
        val g = math.pow(fgg*coeff+lgg*negef, negamma).toInt
        val b = math.pow(fbg*coeff+lbg*negef, negamma).toInt
        //val r = (fr*coeff+lr*negef).toInt
        //val g = (fg*coeff+lg*negef).toInt
        //val b = (fb*coeff+lb*negef).toInt
        val a = (fa*coeff+la*negef).toInt
        buf += new Color(r,g,b,a)
        /*var r = Integer.toHexString(math.round(fr*coeff+lr*negef).toInt)
        var g = Integer.toHexString(math.round(fg*coeff+lg*negef).toInt)
        var b = Integer.toHexString(math.round(fb*coeff+lb*negef).toInt)
        if(r.length()<2)r="0"+r
        if(g.length()<2)g="0"+g
        if(b.length()<2)b="0"+b
        buf += "#"+r+""+g+""+b*/
        //print(r+g+b+" -> ")
      }
    }
    buf.toArray
  }
  
}