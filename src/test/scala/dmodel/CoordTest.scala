package dmodel

//import org.junit.runner.RunWith
//import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

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

  "Coord +" should "give correct result" in
    {
      val coord = Coord(1, 1) + Coord(1, 1)
      assert (coord.equals(Coord(2,2)), "Coord + coord is wrong in 1+1")
      val coord2 = Coord(100,0) + Coord(0, -100)
      assert (coord2.equals(Coord(100, -100)), "Coord + coord is wrong with negative numbers")
      val coord3 = Coord(4.5, 3.5) + Coord(-4.5, 6.5)
      assert (coord3.equals(Coord(0, 10)), "Coord + coord is wrong with negative numbers")
      val coord4 = Coord(7, 24) + Coord(0.01, 0.5359)
      assert (coord4.equals(Coord(7.01, 24.5359)), "Coord + coord is wrong with decimal numbers")
    }


  "Coord.rounded" should "give correct result" in
    {
      var coord = Coord(1.001, 1.59)
      var expected = Coord(1, 1.5)
      var res = coord.rounded(2)
      assert (res.equals(expected), "Coord.rounded fails with accuracy 2: expected "+expected+", got "+res)
      expected = Coord(1, 2)
      res = coord.rounded(1)
      assert (res.equals(expected), "Coord.rounded fails with accuracy 1: expected "+expected+", got "+res)
      expected = Coord(1, 1.6)
      res = coord.rounded(10)
      assert (res.equals(expected), "Coord.rounded fails with accuracy 10: expected "+expected+", got "+res)
      coord = Coord(-0.25, 10005.525)
      expected = Coord(0, 10005.5)
      res = coord.rounded(2)
      assert (res.equals(expected), "Coord.rounded fails with accuracy 2: expected "+expected+", got "+res)
      expected = Coord(-0.2, 10005.5)
      res = coord.rounded(10)
      assert (res.equals(expected), "Coord.rounded fails with accuracy 10: expected "+expected+", got "+res)
    }



}

