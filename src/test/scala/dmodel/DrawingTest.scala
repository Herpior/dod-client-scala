package dmodel


import java.awt.Color

import dmodel.dpart.{BasicLine, BezierLine, EditLine, MultiLine}
import dmodel.tools.{BezierTool, DrawTool}
//import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
//import org.scalatest.junit.JUnitRunner

class DrawingTest extends FlatSpec with Matchers {

  // these were just for checking if the speed increased after improvements, too slow to run with normal tests
  /*
    "DrawTool.onMouseDrag" should "be faster than it was when LineDrawer was using drawPolyline" in {
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


    "BezierTool.onMouseDrag" should "be faster than it was when LineDrawer was using drawPolyline" in {
      val doodleModel = new DoodleModel
      val doodleBufferer = new DoodleBufferer(doodleModel, Magic.x, Magic.y)
      val drawTool = new BezierTool
      val strokes = 50
      val segments = 1000

      val start = System.nanoTime()
      for(j <- 0 to strokes){
        var place = Coord(math.random*Magic.x, math.random*Magic.y)
        drawTool.onMouseDown(doodleBufferer, place, 1, false, false, false)
        for (i <- 0 to segments/3){
          place = Coord(math.random*Magic.x, math.random*Magic.y)
          drawTool.onMouseDrag(doodleBufferer, place, true, false, false, false, false, false)
        }
        drawTool.onMouseUp(doodleBufferer, place, 1, false, false, false)
        for (i <- 0 to segments/3){
          place = Coord(math.random*Magic.x, math.random*Magic.y)
          drawTool.onMouseMove(doodleBufferer, place, false, false, false)
        }
        drawTool.onMouseDown(doodleBufferer, place, 1, false, false, false)
        for (i <- 0 to segments/3){
          place = Coord(math.random*Magic.x, math.random*Magic.y)
          drawTool.onMouseDrag(doodleBufferer, place, true, false, false, false, false, false)
        }
      }
      val end = System.nanoTime()
      println(end-start)
      // stats without path2d
      // 5999636724
      // stats before trying to cache basiclines:
      // 4151529227
      // 3422657743
      // 3120060388
      // stats with caching
      // 3314750087
      // 3619398401
      // 4309356245
      // 6208450668

      val start2 = System.nanoTime()
      for(j <- 0 to strokes){
        doodleModel.undo()
      }
      for(j <- 0 to strokes){
        doodleModel.redo()
      }
      for(j <- 0 to strokes){
        doodleModel.undo()
      }
      val end2 = System.nanoTime()
      println(end2-start2)
      // stats without path2d
      // 23893234
      // 6891633
      // stats before trying to cache basiclines:
      // 16364816
      // 7013265
      // 5008922
      // stats with caching
      // 15038048
      // 6289660
      // 5308915
      // 6717160
      // 4706153

      doodleModel.layers.getCurrent.revive()
      val start3 = System.nanoTime()
      for(j <- 0 to segments){
        doodleBufferer.redrawMid()
      }
      val end3 = System.nanoTime()
      println(end3-start3)
      // stats without path2d
      // 20195396479
      // 24060176213
      // stats before trying to cache basiclines:
      // 24502440375
      // 18852184417
      // 20272827043
      // stats with caching
      // 16834978579
      // 19291435348
      // 16870421731
      // 19425561594
    }
    */

}

