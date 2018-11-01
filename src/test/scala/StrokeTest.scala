package tests

import dmodel.dpart.BasicLine
import org.scalatest.FlatSpec
import org.scalatest.Matchers
//import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.awt.Color
import dmodel._

//@RunWith(classOf[JUnitRunner])
class StrokeTest extends FlatSpec with Matchers {

  "BasicStroke.toString" should "return correctly formatted string" in
    {
      val stroke = new BasicLine(Color.decode("#ff9900"),5)
      stroke.addCoord(Coord(0,0))
      stroke.addCoord(Coord(1,1))
      stroke.addCoord(Coord(1,11))
      stroke.addCoord(Coord(2,22.5))
      stroke.addCoord(Coord(3,389.5))
      assert (stroke.toString().equals("{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"}"),stroke.toString+" not equal to {\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"}")
      val stroke2 = new BasicLine(Color.decode("#abccee"),5)
      stroke2.addCoord(Coord(0,0))
      stroke2.addCoord(Coord(1,1))
      assert (stroke2.toString().equals("{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"}"),stroke.toString+" not equal to {\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"}")
    }

}

