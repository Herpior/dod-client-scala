package dmodel.tools

import java.awt.image.BufferedImage
import java.awt.Color

import collection.mutable.Queue
import dmodel._
import dmodel.dpart.{BasicLine, DoodlePart, MultiLine}

class FillTool extends LineTool{

  
  //override def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean) {
  //  db.redrawDrawing
  //  db.repaint
  //}
  override def onMouseDrag(db:DoodleBufferer, coord:Coord, left:Boolean, middle:Boolean, right:Boolean, control:Boolean, alt:Boolean, shift:Boolean){
    if(Magic.authorized && left){
      dragLine(coord, control, shift)
      db.redrawDrawing
      //db.repaint
    }
  }
  override def onMouseMove(db:DoodleBufferer, coord:Coord, control:Boolean, alt:Boolean, shift:Boolean) {}
  override def onMouseDown(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    if(button == 1){
      startGradient(coord, control, alt, shift)
      db.redrawDrawing
      //db.repaint
    }
  }
  override def onMouseUp(db:DoodleBufferer, coord:Coord, button:Int, control:Boolean, alt:Boolean, shift:Boolean) {
    if(button == 1){
      if(control)fillGradient(db.model, db.getSelected, coord, alt, shift) //next:MultiLine,border:BufferedImage,color1:Color,color2:Color,sizeo:Int,vertical:Boolean,place:Coord,mods:Int
      else addGradient(db.model, coord, alt, shift)
      db.redrawLastMid
      db.redrawDrawing
      //db.repaint
    }
  }
  //override def getLines() = {collection.mutable.Buffer()} //for redrawing the whole line while drawing?
  //override def getLastLine() = {None} // for drawing one segment of the line
  /*def startGradient(next:MultiLine,place:Coord,mods:Int){
    //next = new nextLinee
    //next.strookes += new nextStrooke("#000",1)
    //val s = next.strookes(0)
    //val x = getX(e)
    //val y = getY(e)
      //s.xs += x
      //s.ys += y
  }*/
  
  
  def addGradient(next:MultiLine,color1:Color,color2:Color,sizeo:Int,vertical:Boolean,place:Coord, alt:Boolean, shift:Boolean){
    //val size = side.bsize
    val size = (sizeo+1)/2*2
    val n = if(vertical) (Magic.y*2)/size else (Magic.x*2)/size
    val interval = if(size>32)if(vertical) Magic.y/n else Magic.x/n else size/2
    val colors = Colors.linearcolor(n,!alt,color1,color2)
    for(i<- 0 until n){
      if(vertical){
        val y = size/4+interval*i//Magic.y*i/(n-1)
        next.addLine(new BasicLine(colors(i), size){ this.setCoords(Array(new Coord(0,y), new Coord(Magic.x, y))) })
      } else {
        val x = size/4+interval*i//size/2*(i+1)//Magic.x*i/(n-1)
        next.addLine(new BasicLine(colors(i), size){ this.setCoords(Array(new Coord(x,0), new Coord(x, Magic.y))) })
      }
    }
  }
  //returns image with flood fill in black, lowest y, hightst y - numerically.
  def floodfill(img:BufferedImage,x:Int,y:Int) :(BufferedImage,Coord,Coord) = {
    //println(x+","+y)
    //println(img.getHeight+","+img.getWidth)
    val color = img.getRGB(x, y)
    val color2 = Color.RED.getRGB
    val max = Magic.x*2
    val may = Magic.y*2
    val res = new BufferedImage(max,may,BufferedImage.TYPE_INT_ARGB)
    val g = res.createGraphics()
    //println(max+"mm"+may)
    g.setColor(Color.RED)
    var c0 = new Coord(max,may)
    var c1 = new Coord(0,0)
    //var big = 0
    inner(x,y,new Queue())
    def inner( ix:Int,iy:Int,que:Queue[(Int,Int)] ) {
        if(res.getRGB(ix, iy)!=color2 
            && img.getRGB(ix, iy)==color){
          if(iy<c0.y) c0 = new Coord(c0.x,iy)
          if(iy>c1.y) c1 = new Coord(c1.x,iy)
          if(ix<c0.x) c0 = new Coord(ix,c0.y)
          if(ix>c1.x) c1 = new Coord(ix,c1.y)
          
          g.drawRect(ix, iy, 0, 0)
          if(ix>0){
            que.enqueue((ix-1,iy))
          }
          if(iy>0){
            que.enqueue((ix,iy-1))
          }
          if(ix<max-1){
            que.enqueue((ix+1,iy))
          }
          if(iy<may-1){
            que.enqueue((ix,iy+1))
          }
        }
      
        if(!que.isEmpty){
          val xy = que.dequeue
          inner(xy._1,xy._2,que)
        }
    }
    //println(big+"max"+y2)
    (res,c0,c1)
  }
  
  def fillGradient(next:MultiLine,border:BufferedImage,color1:Color,color2:Color,sizeo:Int,vertical:Boolean,place:Coord, alt:Boolean, shift:Boolean){
    //println(x+","+y)
    val size = (sizeo+1)/2*2
    //val img = new BufferedImage(Magic.x*2,Magic.y*2,BufferedImage.TYPE_INT_ARGB)
    //LineDrawer.redraw(img,layers(current).strokes.toArray)
    //if(layers.length>current+1)redraw2(img,layers(current+1).strokes .toArray)
    
    val flood = floodfill(border,(2*place.x).toInt,(2*place.y).toInt)
    val min = flood._2
    val max = flood._3
    //println("min: "+min)
    //println("max: "+max)
    val dc = max-min
    val flooded = flood._1
    //println(flooded.getHeight+" -|- "+flooded.getWidth)
    val color = flooded.getRGB((2*place.x).toInt,(2*place.y).toInt)
    val n = if(vertical) (dc.y.toInt+1)/(size) else (dc.x.toInt+1)/(size)
    val interval = if(size>32)if(vertical) dc.y.toInt/(n) else dc.x.toInt/(n)else size
    //println("n: "+n)
    //next = new nextLinee
    val colors = Colors.linearcolor(n,!alt,color1,color2)
    //println(dy+" = "+y2+"-"+y1)
    for(j<- 0 until n){
      val o = size/4+interval*j
      val tc = min + Coord(o,o)//dc*j/n
      val iterate = if(vertical)2*Magic.x else 2*Magic.y
      var foundArea = false
      var iterating = 0
      for(i<-0 until iterate){
        if(vertical){
          if(foundArea){
            if(flooded.getRGB(i,tc.y.toInt)==color ){   //previous was in area and this is too
            } else {                            //previous pixel was in area but this is not
              next.addLine(new BasicLine(colors(j), size){ this.setCoords( Array(new Coord(iterating/2.0, tc.y/2),new Coord(i/2.0, tc.y/2)) )})
              foundArea = false
            }
          } else {
            //println(ty1+" , "+i)
            if(flooded.getRGB(i,tc.y.toInt)==color ){ //previous wasn't in area but this is
              foundArea = true
              iterating = i
            } else {                            //previous pixel wasn't in area and this neither
          }
        }
        } else {
          if(foundArea){
            if(flooded.getRGB(tc.x.toInt,i)==color ){   //previous was in area and this is too
            } else {                            //previous pixel was in area but this is not
              next.addLine(new BasicLine(colors(j), size){ this.setCoords( Array(new Coord(tc.x/2,iterating/2.0),new Coord(tc.x/2,i/2.0)) )})
              foundArea = false
            }
          } else {
            //println(tc.x+" , "+i)
            if(flooded.getRGB(tc.x.toInt,i)==color ){ //previous wasn't in area but this is
              foundArea = true
              iterating = i
            } else {                            //previous pixel wasn't in area and this neither
          }
        }
        }
      }
      if(foundArea){
        if (vertical) next.addLine(new BasicLine(colors(j), size){this.setCoords( Array(new Coord(iterating/2.0, tc.y/2), new Coord(Magic.x, tc.y/2)) )})
        else          next.addLine(new BasicLine(colors(j), size){this.setCoords( Array(new Coord(tc.x/2, iterating/2.0), new Coord(tc.x/2, Magic.y)) )})
      }
    }
    //adding = flooded
    //repaint
  }
  
  //---------\\
  private def vertRev(defaultVert:Boolean, defaultRev:Boolean) = {
    try{
      val line = multiLine.getLast.get.getCoords
      val last = line.last
      val first = line.head
      val vert = math.abs(first.x-last.x) < math.abs(first.y-last.y)
      val rever = (vert && first.y < last.y) || (!vert && first.x < last.x)
      (vert, rever)
    } catch {
      case e:Throwable=> (defaultVert, defaultRev)
    }
  }
  //---------\\
  def startGradient(place:Coord, control:Boolean, alt:Boolean, shift:Boolean){
    //multiLine = new MultiLine
    startLine(ColorModel.getColor, 1, place)
    //addLine(ColorModel.getColor, 1, place)
  }
  def fillGradient(model:dmodel.DoodleModel, border:java.awt.image.BufferedImage,place:Coord, alt:Boolean, shift:Boolean){
    val next = new MultiLine
    val (vertical, reverse) = vertRev(shift, false)
    if(reverse) fillGradient(next, border, ColorModel.getColor2, ColorModel.getColor, SizeModel.getSize, vertical, place, alt, shift)
    else        fillGradient(next, border, ColorModel.getColor, ColorModel.getColor2, SizeModel.getSize, vertical, place, alt, shift)
    model.layers.getCurrent.add(next)
    multiLine = new MultiLine
  }
  def addGradient(model:dmodel.DoodleModel, place:Coord, alt:Boolean, shift:Boolean){
    val next = new MultiLine
    val (vertical, reverse) = vertRev(shift, false)
    if(reverse) addGradient(next, ColorModel.getColor2, ColorModel.getColor, SizeModel.getSize, vertical, place, alt, shift)
    else        addGradient(next, ColorModel.getColor, ColorModel.getColor2, SizeModel.getSize, vertical, place, alt, shift)
    model.layers.getCurrent.add(next)
    multiLine = new MultiLine
  }
}