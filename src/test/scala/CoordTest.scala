package tests

import dmodel.dpart.BasicLine
import org.scalatest.FlatSpec
import org.scalatest.Matchers
//import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.awt.Color
import dmodel._

//@RunWith(classOf[JUnitRunner])
class CoordTest extends FlatSpec with Matchers {

  "Coord.toString" should "return correctly formatted string" in
    {
      val coord = Coord(10.0, 5.5)
      assert (coord.toString.equals("(10.0, 5.5)"), "Coord.toString has wrong format: "+coord)
    }


  "Coord.length" should "give correct result" in
    {
      val coord = Coord(10.0, 5.5)
      assert (coord.length.equals(coord.dist(Coord(0))), "Coord.length gives different result than Coord.dist")
      val coord2 = Coord(100,0)
      assert (coord2.length.equals(100.0), "Coord.length is wrong in one dimension")
      val coord3 = Coord(4,3)
      assert (coord3.length.equals(5.0), "Coord.length is wrong in two dimensions")
      val coord4 = Coord(7,24)
      assert (coord4.length.equals(25.0), "Coord.length is wrong in two dimensions")
    }

  "Coord.length" should "give correct result" in
    {
      val coord = Coord(10.0, 5.5)
      assert (coord.length.equals(coord.dist(Coord(0))), "Coord.length gives different result than Coord.dist")
      val coord2 = Coord(100,0)
      assert (coord2.length.equals(100.0), "Coord.length is wrong in one dimension")
      val coord3 = Coord(4,3)
      assert (coord3.length.equals(5.0), "Coord.length is wrong in two dimensions")
      val coord4 = Coord(7,24)
      assert (coord4.length.equals(25.0), "Coord.length is wrong in two dimensions")
    }



}

