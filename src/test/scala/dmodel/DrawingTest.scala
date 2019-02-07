package dmodel


import java.awt.Color

import dmodel.dpart.{BasicLine, BezierLine, EditLine, MultiLine}
import dmodel.tools.DrawTool
//import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
//import org.scalatest.junit.JUnitRunner

//@RunWith(classOf[JUnitRunner])
class DrawingTest extends FlatSpec with Matchers {

  "doodleBufferer.toDodPostJsonStrokes" should "return correctly formatted string for uploading to dod" in {
    val doodleModel = new DoodleModel
    val doodleBufferer = new DoodleBufferer(doodleModel, Magic.x, Magic.y)
    val drawTool = new DrawTool
    val strokes = 50
    val segments = 1000

    val start = System.nanoTime()
    for(j <- 0 to strokes){
    var place = Coord(0)
    drawTool.onMouseDown(doodleBufferer, place, 1, false, false, false)
    for (i <- 0 to segments){
      place = Coord(math.random*Magic.x, math.random*Magic.y)
      drawTool.onMouseDrag(doodleBufferer, place, true, false, false, false, false, false)
    }
    drawTool.onMouseUp(doodleBufferer, place, 1, false, false, false)
    }
    val end = System.nanoTime()
    println(end-start)
    // stats before trying path2d:
    // 3338569329 with println on every redraw
    // 743187232 without, 14863744 per stroke
    // 686591216 without
    // 664722526 without
    // stats with path, broken
    // 513993626
    // 413054810
    // 444163922
    // stats with path, fixed
    // 434902821
    // 414466676
    // 475738562

  }


}

