package dmodel


object Matrix/*(magicX:Int,magicY:Int)*/ {
  /*var m0x = 0.0
  var m1x = 0.0+magicX
  var m2x = 0.0+magicX//-10
  var m3x = 0.0//+10
  
  var m0y = 0.0//+10
  var m1y = 0.0
  var m2y = 0.0+magicY//-100
  var m3y = 0.0+magicY

  val maxx = 520
  val maxy = 390
  */
  /*def fromCorners(source:Array[Coord], destination:Array[Coord]) : Array[Array[Double]] = {
    if(source.size<4 || destination.size<4) return Array(Array(1,0,0),Array(0,1,0),Array(0,0,1))
    val p0 = source(0)
    val p1 = source(1)
    val p2 = source(2)
    val p3 = source(3)
    
    val r0 = destination(0)
    val r1 = destination(1)
    val r2 = destination(2)
    val r3 = destination(3)
    
    val a01 = Array(p0.x,p0.y,1.0,r0.x)
    val a11 = Array(p1.x,p1.y,1.0,r1.x)
    val a21 = Array(p2.x,p2.y,1.0,r2.x)
    
    val a02 = Array(p0.x,p0.y,1.0,r0.y)
    val a12 = Array(p1.x,p1.y,1.0,r1.y)
    val a22 = Array(p2.x,p2.y,1.0,r2.y)
    
    val as1 = Array(a01,a11,a21)
    val as2 = Array(a02,a12,a22)
    
    val res1 = gaussian(as1)
    val res2 = gaussian(as2)
    
    val a03 = Array(res1(0)*p3.x,p3.y,r3.x)//idk what should I even have here
    val a13 = Array(p3.x,p3.y,r3.y)//or here
    val as3 = Array(a03,a13)
    
    
    
    //x = x*a + y*b + c
    //y = x*d + y*e + f
    //w = x*g + y*h + i 
    Array(Array())
  }*/
  
  def gaussian(arr:Array[Array[Double]]) = {
    val as = arr.map(_.clone())
    println("as:")
    println(as.map(_.mkString(", ")).mkString("\n"))
    
    println("red:")
    for( i<- as.indices.dropRight(1)){
      val tmp = 1.0/as(i)(i)
      for(j<-as(i).indices){
        as(i)(j) *= tmp
        //println(as(i)(j))
      }
      for(row <- as.drop(i+1)){
        println(row.mkString(", "))
        val coeff = row(i)
        for(j <- i until row.size){
          row(j) -= coeff * as(i)(j)
        }
        println(row.mkString(", "))
      }
    }
    as.last.last
    println("mid:")
    println(as.map(_.mkString(", ")).mkString("\n"))
    println("idek:")//not working yet
    for( i <- as.indices.drop(1).reverse){
      val tmp = 1.0/as(i)(i)
      for(j<-as(i).indices){
        as(i)(j) *= tmp
        //println(as(i)(j))
      }
      for(row <- as.take(i)){
        val coeff = row(i)
        for(j <- i until row.size){
          row(j) -= coeff * as(i)(j)
        }
        println(row.mkString(", "))
      }
    }
    print("res: "+as.map(_.last).mkString(", "))
  }
  /*//for testing gaussian
  var arr = Array(Array(1.0,3.0,-2.0,5.0),Array(3.0,5.0,6.0,7.0),Array(2.0,4.0,3.0,8.0))
gaussian (arr)
  */
  /*def fromCorners(x0:Double,y0:Double,x1:Double,y1:Double,x2:Double,y2:Double,x3:Double,y3:Double)={
    val l0 = Array(x0,y0,1)
    val l1 = Array(x1,y1,1)
    val l2 = Array(x2,y2,1)
    val l3 = Array(x3,y3,1)
    
    val a0 = Array(0,0,1.0)
    val a1 = Array(maxx,0,1.0)
    val a2 = Array(0,maxy,1.0)
    val a3 = Array(maxx,maxy,1.0)
    
    
    
  }*/
  
  def transferPoint (source:Array[Coord], destination:Array[Coord]) : Coord=>Coord =
{
    
    def inner(c:Coord) = {
        
      var ADDING = 0.001; // to avoid dividing by zero
  
      var xA = source(0).x;
      var yA = source(0).y;
  
      var xC = source(2).x;
      var yC = source(2).y;
  
      var xAu = destination(0).x;
      var yAu = destination(0).y;
  
      var xBu = destination(1).x;
      var yBu = destination(1).y;
  
      var xCu = destination(2).x;
      var yCu = destination(2).y;
  
      var xDu = destination(3).x;
      var yDu = destination(3).y;
  
      // Calcultations
      // if points are the same, have to add a ADDING to avoid dividing by zero
      if (xBu==xCu) xCu+=ADDING;
      if (xAu==xDu) xDu+=ADDING;
      if (xAu==xBu) xBu+=ADDING;
      if (xDu==xCu) xCu+=ADDING;
      var kBC = (yBu-yCu)/(xBu-xCu);
      var kAD = (yAu-yDu)/(xAu-xDu);
      var kAB = (yAu-yBu)/(xAu-xBu);
      var kDC = (yDu-yCu)/(xDu-xCu);
  
      if (kBC==kAD) kAD+=ADDING;
      var xE = (kBC*xBu - kAD*xAu + yAu - yBu) / (kBC-kAD);
      var yE = kBC*(xE - xBu) + yBu;
  
      if (kAB==kDC) kDC+=ADDING;
      var xF = (kAB*xBu - kDC*xCu + yCu - yBu) / (kAB-kDC);
      var yF = kAB*(xF - xBu) + yBu;
  
      if (xE==xF) xF+=ADDING;
      var kEF = (yE-yF) / (xE-xF);
  
      if (kEF==kAB) kAB+=ADDING;
      var xG = (kEF*xDu - kAB*xAu + yAu - yDu) / (kEF-kAB);
      var yG = kEF*(xG - xDu) + yDu;
  
      if (kEF==kBC) kBC+=ADDING;
      var xH = (kEF*xDu - kBC*xBu + yBu - yDu) / (kEF-kBC);
      var yH = kEF*(xH - xDu) + yDu;

      
      val xI = c.x
      val yI = c.y
        
      var rG = (yC-yI)/(yC-yA);
      var rH = (xI-xA)/(xC-xA);
  
      var xJ = (xG-xDu)*rG + xDu;
      var yJ = (yG-yDu)*rG + yDu;
  
      var xK = (xH-xDu)*rH + xDu;
      var yK = (yH-yDu)*rH + yDu;
  
      if (xF==xJ) xJ+=ADDING;
      if (xE==xK) xK+=ADDING;
      var kJF = (yF-yJ) / (xF-xJ); //23
      var kKE = (yE-yK) / (xE-xK); //12
  
      //var xKE;
      if (kJF==kKE) kKE+=ADDING;
      var xIu = (kJF*xF - kKE*xE + yE - yF) / (kJF-kKE);
      var yIu = kJF * (xIu - xJ) + yJ;
      
      Coord(xIu, yIu)

    }
    
    inner
}
  
}
