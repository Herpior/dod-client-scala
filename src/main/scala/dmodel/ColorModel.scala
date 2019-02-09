package dmodel

/**
  * An object that contains the palette of colours and the indices of the selected colours.
  *

  * @author Qazhax
  */

import java.awt.Color
import math.min

object ColorModel {

  private var colorpicker = 0
  private var colorpicker2 = 0
          
  def getColor = getColors(colorpicker)
  def getColor2 = getColors(colorpicker2)
  
  
  def colorSize: Int = this.getColors.length
  //val rows = 5
  def rows: Int = if(Magic.authorized)Magic.rows else 2
  //val nrows = 2
  private val normal = Array(
          "#000000","#6d6f71","#0791cd","#699c41","#f47e20","#d6163b","#6e1a11","#f8ded7",
          "#ffffff","#d1d3d4","#73cff2","#9bcc66","#ffec00","#f180aa","#812468","#8e684c").map { x => Colors.toColor(x) }
  if(Magic.namira) normal(7) = Colors.toColor("#ede4c8")
  private val superColours = normal ++ Array(
    "#000000","#111111","#222222","#333333","#444444","#555555","#666666","#777777","#888888","#999999","#aaaaaa","#bbbbbb","#cccccc","#dddddd","#eeeeee","#ffffff",
    "#f69679","#f9ad81","#fdc689","#fff799","#c4df9b","#a3d39c","#82ca9c","#7accc8","#6dcff6","#7da7d9","#8393ca","#8781bd","#a186be","#bd8cbf","#f49ac1","#f5989d",
    "#f26c4f","#f68e56","#fbaf5d","#fff568","#acd373","#7cc576","#3cb878","#1cbbb4","#00bff3","#448ccb","#5674b9","#605ca8","#8560a8","#a864a8","#f06eaa","#f26d7d",
    "#ed1c24","#f26522","#f7941d","#fff200","#8dc63f","#39b54a","#00a651","#00a99d","#00aeef","#0072bc","#0054a6","#2e3192","#662d91","#92278f","#ec008c","#ed145b",
    "#9e0b0f","#a0410d","#a3620a","#aba000","#598527","#197b30","#007236","#00726b","#0076a3","#004a80","#003471","#1b1464","#440e62","#630460","#9e005d","#9e0039",
    "#790000","#7b2e00","#7d4900","#827b00","#406618","#005e20","#005826","#005952","#005b7f","#003663","#002157","#0d004c","#32004b","#4b0049","#7b0046","#7a0026",
    "#ede4c8","#ffdcb1","#e5c8a8","#e4b98e","#e3a173","#d0926e","#bb754d","#6a4e42","#54382c","#2a201c","#362f2d","#534741","#736357","#998675","#c0a994","#ecd5c0"
  ).map { x => Colors.toColor(x) }
  private def load = {
    val loaded = try io.LocalStorage.readArray("colours").map { x => Colors.toColor(x) }
                 catch{
                   case e:Throwable=>
                     if(Magic.authorized) superColours else normal
                 }
    val empty = Array.fill(16*Magic.rows)(Color.black)
    for(i<-0 until min(loaded.length, 16*Magic.rows)){
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
  def getColors: Array[Color] = if(Magic.authorized)colors else normal
  def rowl: Int = colorSize/rows//(colors.length/rows)
  def colorIndex: Int = colorpicker
  def colorIndex2: Int = colorpicker2
  //def colorIndex2 = colorpicker2
  //def getColor(ind:Int) = if(ind<1)getColors(colorIndex) else getColors(colorIndex2)
  def colorUp(): Unit =  if(colorpicker>=rowl){
    if(colorpicker2 == colorpicker) colorpicker2 -= rowl
    colorpicker -= rowl
  }
  def colorDown(): Unit = if(colorpicker<rowl*(rows-1)){
    if(colorpicker2 == colorpicker) colorpicker2 += rowl
    colorpicker += rowl
  } 
  def colorLeft(): Unit = if(colorpicker>0){
    if(colorpicker2 == colorpicker) colorpicker2 -= 1
    colorpicker -= 1
  }
  def colorRight(): Unit = if(colorpicker<colorSize-1){
    if(colorpicker2 == colorpicker) colorpicker2 += 1
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
      case e:Throwable=>e.printStackTrace()
    }
  }
  def setColor(color:Color){
    if(Magic.authorized){
      colors(colorpicker)= color
      io.LocalStorage.storeArray(colors.map(color=>Colors.toHexRGBA(color)), "colours")
    }
  }
}