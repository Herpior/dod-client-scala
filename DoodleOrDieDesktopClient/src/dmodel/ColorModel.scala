package dmodel

import java.awt.Color
import math.min
//import collection.mutable.Buffer

object ColorModel {

  private var colorpicker = 0
  private var colorpicker2 = 0
          
  def getColor = getColors(colorpicker)
  def getColor2 = getColors(colorpicker2)
  
  
  def colorSize = this.getColors.length
  //val rows = 5
  def rows = if(Magic.authorized)8 else 2
  //val nrows = 2
  private val normal = Array(
          "#000000","#6d6f71","#0791cd","#699c41","#f47e20","#d6163b","#6e1a11","#f8ded7",
          "#ffffff","#d1d3d4","#73cff2","#9bcc66","#ffec00","#f180aa","#812468","#8e684c").map { x => Colors.toColor(x) }
  private def load = {
    val loaded = try io.LocalStorage.readArray("colours").map { x => Colors.toColor(x) }
                 catch{
                   case e:Throwable=>
                     normal
                 }
    val empty = Array.fill(16*8)(Color.black)
    for(i<-0 until min(loaded.length, 16*8)){
      val tmp = loaded(i)
      empty(i)=tmp
    }
    empty
  }
  private val colors = load/*normal++Array(
          "#f8ded7","#d9b5a7","#c19986","#a67e65","#8e684c","#824a32","#783321","#6e1a11",
          "#000000","#f7c1ad","#f6ab8a","#f58f4f","#f47e20","#c04e1b","#9e3517","#842514",
          "#000000","#000000","#000000","#000000","#000000","#e2241a","#cc2419","#a62116")*/
    //Array()
  def getColors= if(Magic.authorized)colors else normal
  def rowl = colorSize/rows//(colors.length/rows)
  def colorIndex = colorpicker
  def colorIndex2 = colorpicker2
  //def colorIndex2 = colorpicker2
  //def getColor(ind:Int) = if(ind<1)getColors(colorIndex) else getColors(colorIndex2)
  def colorUp =  if(colorpicker>=rowl){
    colorpicker -= rowl
  }
  def colorDown = if(colorpicker<rowl*(rows-1)){
    colorpicker += rowl
  } 
  def colorLeft = if(colorpicker>0){
    colorpicker -= 1
  }
  def colorRight = if(colorpicker<colorSize-1){
    colorpicker += 1
  }
  def secondaryColor(coord:Coord,bounds:Coord){
    val index = coord.y.toInt*rows/bounds.y.toInt *rowl + coord.x.toInt*rowl/bounds.x.toInt
    secondaryColor(index)
    //min(getColors.length-1,max(0,(coord.y.toInt*rows/bounds.y.toInt*rowl+coord.x.toInt*rowl/bounds.x.toInt).toInt))
  }
  def secondaryColor(index:Int){
    if(Magic.authorized && index>=0 && index<colorSize)colorpicker2 = index
  }
  def primaryColor(coord:Coord,bounds:Coord){
    val index = coord.y.toInt*rows/bounds.y.toInt *rowl + coord.x.toInt*rowl/bounds.x.toInt
    primaryColor(index)
  }
  def primaryColor(index:Int){
    if(index>=0 && index<colorSize){
      colorpicker =  index
      colorpicker2 = index
    }
  }
  
  def setColor(colorr:String){
    if(colorr.length<3||colorr.length>9|| !Magic.authorized) return
    val col = if(colorr.head != '#')"#"+colorr.toLowerCase() else colorr.toLowerCase()
    if(!col.tail.forall(c=>Magic.hexa.contains(c))) return
    try{
      colors(colorpicker)=Colors.toColor(col)
      io.LocalStorage.storeArray(colors.map(color=>Colors.toHexRGBA(color)), "colours")
    }catch{
      case e=>e.printStackTrace
    }
  }
  def setColor(color:Color){
    if(Magic.authorized){
      colors(colorpicker)= color
      io.LocalStorage.storeArray(colors.map(color=>Colors.toHexRGBA(color)), "colours")
    }
  }
}