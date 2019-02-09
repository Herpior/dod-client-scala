package dmodel
/**
  * An object that can be used to compute polyline representations of bezier curves.
  *
  * code translated from http://www.antigrain.com/research/adaptive_bezier/index.html

  */
import math._
import scala.collection.mutable

object Bezier {

  val m_distance_tolerance = 0.25
  


    def pointAt(t:Double,c1:Coord,c2:Coord,c3:Coord,c4:Coord): Coord ={
        val nt = 1-t
        val c12   = c1*t + c2*nt
        val c23   = c2*t + c3*nt
        val c34   = c3*t + c4*nt
        val c123  = c12*t + c23*nt
        val c234  = c23*t + c34*nt
        c123*t + c234*nt
  }
    def curve (coord1:Coord,coord2:Coord,coord3:Coord,coord4:Coord): mutable.Buffer[Coord] = {
      val cbuf = mutable.Buffer[Coord]()
      add_point(coord1)
       recursive_bezier(coord1,coord2,coord3,coord4)
      add_point(coord4)
      
      def add_point(coord:Coord){
        cbuf += coord
      }
      
      def recursive_bezier(c1:Coord,c2:Coord,c3:Coord,c4:Coord) {
        //point 1 = start, point 2 = first guide
        //point 3 = second guide, point 4 = end
        
        // Calculate all the mid-points of the line segments
        val c12   = (c1 + c2) / 2
        val c23   = (c2 + c3) / 2
        val c34   = (c3 + c4) / 2
        val c123  = (c12 + c23) / 2
        val c234  = (c23 + c34) / 2
        val c1234 = (c123 + c234) / 2
        
        
        // Try to approximate the full cubic curve by a single straight line
        //------------------
        val dc = c4-c1
    
        val d2 = abs((c2.x - c4.x) * dc.y - (c2.y - c4.y) * dc.x)
        val d3 = abs((c3.x - c4.x) * dc.y - (c3.y - c4.y) * dc.x)
    
        if((d2 + d3)*(d2 + d3) < m_distance_tolerance * (dc.x*dc.x + dc.y*dc.y)){
          add_point(c1234)
          return
        }
        // Continue subdivision
        //----------------------
        recursive_bezier(c1, c12, c123, c1234) 
        recursive_bezier(c1234, c234, c34, c4) 
      }
      cbuf
    }
    
    
  
}