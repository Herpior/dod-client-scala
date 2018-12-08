package dmodel

//import org.junit.runner.RunWith
//import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

//@RunWith(classOf[JUnitRunner])
class CoordTest extends FlatSpec with Matchers {

  "Coord.toString" should "return correctly formatted string" in
    {
      var coord = Coord(10.1, 5.5)
      assert (coord.toString.equals("(10.1, 5.5)"), "Coord.toString has wrong format: "+coord.toString)
      coord = Coord(10, 5.0)
      assert (coord.toString.equals("(10, 5)"), "Coord.toString has wrong format: "+coord.toString)
      coord = Coord(10, 5.5432154321)
      assert (coord.toString.equals("(10, 5.5432154321)"), "Coord.toString has wrong format: "+coord.toString)
      coord = Coord(-10.0, -5.54321)
      assert (coord.toString.equals("(-10, -5.54321)"), "Coord.toString has wrong format: "+coord.toString)
    }

  "Coord.toShortJsonString" should "return correctly formatted string" in
    {
      var coord = Coord(10.1, 5.5)
      assert (coord.toShortJsonString.equals("10.1,5.5"), "Coord.toShortJsonString has wrong format: "+coord.toShortJsonString)
      coord = Coord(10, 5.0)
      assert (coord.toShortJsonString.equals("10,5"), "Coord.toShortJsonString has wrong format: "+coord.toShortJsonString)
      coord = Coord(10, 5.5432154321)
      assert (coord.toShortJsonString.equals("10,5.5432154321"), "Coord.toShortJsonString has wrong format: "+coord.toShortJsonString)
      coord = Coord(-10.0, -5.54321)
      assert (coord.toShortJsonString.equals("-10,-5.54321"), "Coord.toShortJsonString has wrong format: "+coord.toShortJsonString)
    }

  "Coord.toJsonString" should "return correctly formatted string" in
    {
      var coord = Coord(10.1, 5.5)
      assert (coord.toJsonString.equals("{\"x\":10.1,\"y\":5.5}"), "Coord.toJsonString has wrong format: "+coord.toJsonString)
      coord = Coord(10, 5.0)
      assert (coord.toJsonString.equals("{\"x\":10,\"y\":5}"), "Coord.toJsonString has wrong format: "+coord.toJsonString)
      coord = Coord(10, 5.5432154321)
      assert (coord.toJsonString.equals("{\"x\":10,\"y\":5.5432154321}"), "Coord.toJsonString has wrong format: "+coord.toJsonString)
      coord = Coord(-10.0, -5.54321)
      assert (coord.toJsonString.equals("{\"x\":-10,\"y\":-5.54321}"), "Coord.toJsonString has wrong format: "+coord.toJsonString)
    }


  "- Coord" should "give correct result" in
    {
      val coord = -Coord(1, 1)
      assert (coord.equals(Coord(-1)), "- coord is wrong in 1,1: "+coord)
      val coord2 = -Coord(1e9,0)
      assert (coord2.equals(Coord(-1e9, 0)), "- coord is wrong with largish numbers: "+coord2)
      val coord3 = -Coord(-4.5, 6.5)
      assert (coord3.equals(Coord(4.5, -6.5)), "- coord is wrong with negative numbers: "+coord3)
      val coord4 = -Coord(0.01, 0.5359)
      assert (coord4.equals(Coord(-0.01, -0.5359)), "- coord is wrong with decimal numbers: "+coord4)
      val coord5 = -Coord(0.0, 0)
      assert (coord5.equals(Coord(0)), "- coord is wrong with zeroes")
    }

  "Coord +" should "give correct result" in
    {
      var coord = Coord(1, 1) + Coord(1, 1)
      assert (coord.equals(Coord(2,2)), "Coord + coord is wrong in 1+1: "+coord)
      coord = Coord(100,0) + Coord(0, -100)
      assert (coord.equals(Coord(100, -100)), "Coord + coord is wrong with negative numbers: "+coord)
      coord = Coord(4.5, 3.5) + Coord(-4.5, 6.5)
      assert (coord.equals(Coord(0, 10)), "Coord + coord is wrong with negative numbers: "+coord)
      coord = Coord(7, 24) + Coord(0.01, 0.5359)
      assert (coord.equals(Coord(7.01, 24.5359)), "Coord + coord is wrong with decimal numbers: "+coord)
      coord = Coord(7, 24) + Coord(0.0, 0)
      assert (coord.equals(Coord(7, 24)), "Coord + coord is wrong with zeroes: "+coord)
      coord = Coord(0) + Coord(0.0, 0)
      assert (coord.equals(Coord(0)), "Coord + coord is wrong with zeroes: "+coord)
      coord = Coord(0) + Coord(0.1, -500)
      assert (coord.equals(Coord(0.1,-500)), "Coord + coord is wrong with zeroes: "+coord)
    }

  "Coord -" should "give correct result" in
    {
      var coord = Coord(1, 1) - Coord(1, 1)
      assert (coord.equals(Coord(0)), "Coord - coord is wrong in 1-1: "+coord)
      coord = Coord(100,0) - Coord(0, -100)
      assert (coord.equals(Coord(100, 100)), "Coord - coord is wrong with negative numbers: "+coord)
      coord = Coord(4.5, 3.5) - Coord(-4.5, 6.5)
      assert (coord.equals(Coord(9, -3)), "Coord - coord is wrong with negative numbers: "+coord)
      coord = Coord(7, 24) - Coord(0.01, 0.5359)
      assert (coord.equals(Coord(6.99, 23.4641)), "Coord - coord is wrong with decimal numbers: "+coord)
      coord = Coord(7, 24) - Coord(0.0, 0)
      assert (coord.equals(Coord(7, 24)), "Coord - coord is wrong with zeroes: "+coord)
      coord = Coord(0) - Coord(0.0, 0)
      assert (coord.equals(Coord(0)), "Coord - coord is wrong with zeroes: "+coord)
      coord = Coord(0) - Coord(0.1, -500)
      assert (coord.equals(Coord(-0.1,500)), "Coord - coord is wrong with zeroes: "+coord)
    }

  "Coord *" should "give correct result" in
    {
      var coord = Coord(1, 1) * Coord(1, 1)
      assert (coord.equals(Coord(1)), "Coord * coord is wrong in 1*1: "+coord)
      coord = Coord(100,0) * Coord(0, -100)
      assert (coord.equals(Coord(0)), "Coord * coord is wrong with zeroes: "+coord)
      coord = Coord(4, -3) * Coord(-4, 6)
      assert (coord.equals(Coord(-16, -18)), "Coord * coord is wrong with negative numbers: "+coord)
      coord = Coord(7, 24) * Coord(0.1, 0.5)
      assert (coord.dist(Coord(0.7, 12)) <1e-9, "Coord * coord is wrong with decimal numbers: "+coord)
      coord = Coord(7, 24) * Coord(0.0, 0)
      assert (coord.equals(Coord(0)), "Coord * coord is wrong with zeroes: "+coord)
      coord = Coord(0) * Coord(0.0, 0)
      assert (coord.equals(Coord(0)), "Coord * coord is wrong with zeroes: "+coord)
      coord = Coord(0) * Coord(0.1, -500)
      assert (coord.equals(Coord(0)), "Coord * coord is wrong with zeroes: "+coord)
    }

  "Coord /" should "give correct result" in
    {
      var coord = Coord(1, 1) / Coord(1, 1)
      assert (coord.equals(Coord(1)), "Coord / coord is wrong in 1,1: "+coord)
      coord = Coord(4, -3) / Coord(-4, 6)
      assert (coord.equals(Coord(-1, -0.5)), "Coord / coord is wrong with negative numbers: "+coord)
      coord = Coord(7, 24) / Coord(0.1, 0.5)
      assert (coord.equals(Coord(70, 48)), "Coord / coord is wrong with decimal numbers: "+coord)
      //assertThrows(Coord(0) / Coord(0.0, 0))
      coord = Coord(0) / Coord(0.1, -500)
      assert (coord.equals(Coord(0,0)), "Coord / coord is wrong with zeroes: "+coord)
    }

  "Coord *" should "give correct result with doubles" in
    {
      var coord = Coord(1, 1) * 1
      assert (coord.equals(Coord(1)), "Coord * double is wrong in 1-1: "+coord)
      coord = Coord(100,0) * -100
      assert (coord.equals(Coord(-10000, 0)), "Coord * double is wrong with negative numbers: "+coord)
      coord = Coord(4, 3) * -4
      assert (coord.equals(Coord(-16, -12)), "Coord * double is wrong with negative numbers: "+coord)
      coord = Coord(7, 24) * 0.01
      assert (coord.dist(Coord(0.07, 0.24))<1e-9, "Coord * double is wrong with decimal numbers: "+coord)
      coord = Coord(7, 24) * 0
      assert (coord.equals(Coord(0)), "Coord * double is wrong with zeroes: "+coord)
      coord = Coord(0) * 0
      assert (coord.equals(Coord(0)), "Coord * double is wrong with zeroes: "+coord)
      coord = Coord(0) * 500
      assert (coord.equals(Coord(0)), "Coord * double is wrong with zeroes: "+coord)
    }

  "Coord /" should "give correct result with doubles" in
    {
      var coord = Coord(1, 1) / 1
      assert (coord.equals(Coord(1)), "Coord / double is wrong in 1-1: "+coord)
      coord = Coord(100,0) / -100
      assert (coord.equals(Coord(-1, 0)), "Coord / double is wrong with negative numbers: "+coord)
      coord = Coord(4.5, -9) / 4.5
      assert (coord.equals(Coord(1, -2)), "Coord / double is wrong with negative numbers: "+coord)
      coord = Coord(7, 24) / 0.01
      assert (coord.equals(Coord(700, 2400)), "Coord / double is wrong with decimal numbers: "+coord)
      //coord = Coord(7, 24) / 0
      //assert (coord.equals(Coord(7, 24)), "Coord / double is wrong with zeroes: "+coord)
      coord = Coord(0) / 0.005
      assert (coord.equals(Coord(0)), "Coord / double is wrong with zeroes: "+coord)
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

  "Coord.normalized" should "give correct result" in
    {
      var coord = Coord(1, 1).normalized
      assert (coord.dist(Coord(math.sqrt(2)*0.5))<1e-9, "coord.normalized is wrong in 1,1: "+coord)
      coord = Coord(100, 100).normalized
      assert (coord.dist(Coord(math.sqrt(2)*0.5))<1e-9, "coord.normalized is wrong in 100,100: "+coord)
      coord = Coord(1e9,0).normalized
      assert (coord.equals(Coord(1, 0)), "coord.normalized is wrong with largish numbers: "+coord)
      coord = Coord(-10, 0).normalized
      assert (coord.equals(Coord(-1, 0)), "coord.normalized is wrong with negative numbers: "+coord)
      coord = Coord(0, -468).normalized
      assert (coord.equals(Coord(0, -1)), "coord.normalized is wrong with negative numbers: "+coord)
      coord = Coord(-1, -1).normalized
      assert (coord.dist(Coord(-math.sqrt(2)*0.5))<1e-9, "coord.normalized is wrong in -1,-1: "+coord)
      coord = Coord(0.01, 0).normalized
      assert (coord.equals(Coord(1, 0)), "coord.normalized is wrong with small numbers: "+coord)
      coord = Coord(0.001, 0.001).normalized
      assert (coord.dist(Coord(math.sqrt(2)*0.5))<1e-9, "coord.normalized is wrong in 0.001,0.001: "+coord)
      coord = Coord(0.0, 0).normalized
      assert (coord.equals(Coord(0)), "coord.normalized is wrong with zeroes")
    }

  "Coord.flipped" should "give correct result" in
    {
      var coord = Coord(1, 1).flipped
      assert (coord.equals(Coord(1, 1)), "coord.flipped is wrong in 1,1: "+coord)
      coord = Coord(100, 100).flipped
      assert (coord.equals(Coord(100, 100)), "coord.flipped is wrong in 100,100: "+coord)
      coord = Coord(-1, -1).flipped
      assert (coord.equals(Coord(-1, -1)), "coord.flipped is wrong in -1,-1: "+coord)
      coord = Coord(0.001, 0.001).flipped
      assert (coord.equals(Coord(0.001)), "coord.flipped is wrong in 0.001,0.001: "+coord)
      coord = Coord(0.0, 0).flipped
      assert (coord.equals(Coord(0)), "coord.flipped is wrong in 0,0")
      coord = Coord(1e9,0).flipped
      assert (coord.equals(Coord(0, 1e9)), "coord.flipped is wrong in 1e9,1e9: "+coord)
      coord = Coord(-10, 0).flipped
      assert (coord.equals(Coord(0, -10)), "coord.flipped is wrong with negative numbers: "+coord)
      coord = Coord(0, -468).flipped
      assert (coord.equals(Coord(-468, 0)), "coord.flipped is wrong with negative numbers: "+coord)
      coord = Coord(0.01, 0).flipped
      assert (coord.equals(Coord(0, 0.01)), "coord.flipped is wrong with small numbers: "+coord)
      coord = Coord(1, -1).flipped
      assert (coord.equals(Coord(-1, 1)), "coord.flipped is wrong in 1, -1: "+coord)
    }

  "Coord.perpendiculated" should "give correct result" in
    {
      var coord = Coord(1, 1).perpendiculated
      assert (coord.equals(Coord(-1, 1)), "coord.perpendiculated is wrong in 1,1: "+coord)
      coord = Coord(100, 100).perpendiculated
      assert (coord.equals(Coord(-100, 100)), "coord.perpendiculated is wrong in 100,100: "+coord)
      coord = Coord(-1, -1).perpendiculated
      assert (coord.equals(Coord(1, -1)), "coord.perpendiculated is wrong in -1,-1: "+coord)
      coord = Coord(0.001, 0.001).perpendiculated
      assert (coord.equals(Coord(-0.001, 0.001)), "coord.perpendiculated is wrong in 0.001,0.001: "+coord)
      coord = Coord(0.0, 0).perpendiculated
      assert (coord.equals(Coord(0)), "coord.perpendiculated is wrong in 0,0")
      coord = Coord(1e9,0).perpendiculated
      assert (coord.equals(Coord(0, 1e9)), "coord.perpendiculated is wrong in 1e9,1e9: "+coord)
      coord = Coord(-10, 0).perpendiculated
      assert (coord.equals(Coord(0, -10)), "coord.perpendiculated is wrong with negative numbers: "+coord)
      coord = Coord(0, -468).perpendiculated
      assert (coord.equals(Coord(468, 0)), "coord.perpendiculated is wrong with negative numbers: "+coord)
      coord = Coord(0.01, 0).perpendiculated
      assert (coord.equals(Coord(0, 0.01)), "coord.perpendiculated is wrong with small numbers: "+coord)
      coord = Coord(0, 0.01).perpendiculated
      assert (coord.equals(Coord(-0.01, 0)), "coord.perpendiculated is wrong with small numbers: "+coord)
      coord = Coord(1, -1).perpendiculated
      assert (coord.equals(Coord(1, 1)), "coord.perpendiculated is wrong in 1, -1: "+coord)
    }


  "Coord.length" should "give correct result" in
    {
      var coord = Coord(100,0)
      assert (coord.length.equals(100.0), "Coord.length is wrong in one dimension: "+coord.length)
      coord = Coord(4,3)
      assert (coord.length.equals(5.0), "Coord.length is wrong in two dimensions: "+coord.length)
      coord = Coord(7,24)
      assert (coord.length.equals(25.0), "Coord.length is wrong in two dimensions: "+coord.length)
      coord= Coord(10.0, 5.5)
      assert (math.abs(coord.length - 11.41271221051332274056) < 1e-9, "Coord.length wrong in harder case: "+coord.length)
      coord = Coord(0)
      assert (coord.length.equals(0.0), "Coord.length is wrong in 0,0: "+coord.length)
    }

  "Coord.lengthSquared" should "give correct result" in
    {
      var coord = Coord(10.0, 5.5)
      assert (coord.length.equals(coord.dist(Coord(0))), "Coord.length gives different result than Coord.dist: "+coord.length)
      coord = Coord(100,0)
      assert (coord.length.equals(100.0), "Coord.length is wrong in one dimension: "+coord.length)
      coord = Coord(4,3)
      assert (coord.length.equals(5.0), "Coord.length is wrong in two dimensions: "+coord.length)
      coord = Coord(7,24)
      assert (coord.length.equals(25.0), "Coord.length is wrong in two dimensions: "+coord.length)
      coord = Coord(0)
      assert (coord.length.equals(0.0), "Coord.length is wrong in 0,0. got: "+coord.length)
    }

  "Coord.dist" should "give correct result" in
    {
      val epsilon = 1e-9
      var target = Coord(1, 1)
      var coord = Coord(1, 1)
      var expected = 0.0
      var res = coord.dist(target)
      assert (res.equals(expected), "Coord.dist fails with both in one spot: expected "+expected+", got "+res)
      target = Coord(2)
      expected = math.sqrt(2)
      res = coord.dist(target)
      assert (math.abs(res-expected) < epsilon, "Coord.dist fails with diagonal dist: expected "+expected+", got "+res)
      target = Coord(-1)
      expected = math.sqrt(2)*2
      res = coord.dist(target)
      assert (math.abs(res-expected) < epsilon, "Coord.dist fails with diagonal dist: expected "+expected+", got "+res)
      target = Coord(0,1)
      expected = 1
      res = coord.dist(target)
      assert (res.equals(expected), "Coord.dist fails with vertical dist: expected "+expected+", got "+res)
      target = Coord(3,1)
      expected = 2
      res = coord.dist(target)
      assert (res.equals(expected), "Coord.dist fails with vertical dist: expected "+expected+", got "+res)
      target = Coord(1,0)
      expected = 1
      res = coord.dist(target)
      assert (res.equals(expected), "Coord.dist fails with horizontal line: expected "+expected+", got "+res)
      target = Coord(1,5)
      expected = 4
      res = coord.dist(target)
      assert (res.equals(expected), "Coord.dist fails with horizontal line: expected "+expected+", got "+res)
      target = Coord(5,1)
      coord = Coord(-5,1)
      expected = 10
      res = coord.dist(target)
      assert (res.equals(expected), "Coord.dist fails with negative x on coord: expected "+expected+", got "+res)
    }

  "Coord.distSquared" should "give correct result" in
    {
      val epsilon = 1e-9
      var target = Coord(1, 1)
      var coord = Coord(1, 1)
      var expected = 0.0
      var res = coord.distSquared(target)
      assert (res.equals(expected), "Coord.distSquared fails with both in one spot: expected "+expected+", got "+res)
      target = Coord(2)
      expected = 2
      res = coord.distSquared(target)
      assert (math.abs(res-expected) < epsilon, "Coord.distSquared fails with diagonal distSquared: expected "+expected+", got "+res)
      target = Coord(-1)
      expected = 8
      res = coord.distSquared(target)
      assert (math.abs(res-expected) < epsilon, "Coord.distSquared fails with diagonal distSquared: expected "+expected+", got "+res)
      target = Coord(0,1)
      expected = 1
      res = coord.distSquared(target)
      assert (res.equals(expected), "Coord.distSquared fails with vertical distSquared: expected "+expected+", got "+res)
      target = Coord(3,1)
      expected = 4
      res = coord.distSquared(target)
      assert (res.equals(expected), "Coord.distSquared fails with vertical distSquared: expected "+expected+", got "+res)
      target = Coord(1,0)
      expected = 1
      res = coord.distSquared(target)
      assert (res.equals(expected), "Coord.distSquared fails with horizontal line: expected "+expected+", got "+res)
      target = Coord(1,5)
      expected = 16
      res = coord.distSquared(target)
      assert (res.equals(expected), "Coord.distSquared fails with horizontal line: expected "+expected+", got "+res)
      target = Coord(5,1)
      coord = Coord(-5,1)
      expected = 100
      res = coord.distSquared(target)
      assert (res.equals(expected), "Coord.distSquared fails with negative x on coord: expected "+expected+", got "+res)
    }

  "Coord.distFromLine" should "give correct result" in
    {
      val epsilon = 1e-9
      var start = Coord(1, 1)
      var end = Coord(1, 1)
      var coord = Coord(1, 1)
      var expected = 0.0
      var res = coord.distFromLine(start, end)
      assert (res.equals(expected), "Coord.distFromLine fails with all in one spot: expected "+expected+", got "+res)
      start = Coord(0.0)
      res = coord.distFromLine(start, end)
      assert (res.equals(expected), "Coord.distFromLine fails with end = target: expected "+expected+", got "+res)
      end = Coord(2,2)
      res = coord.distFromLine(start, end)
      assert (res.equals(expected), "Coord.distFromLine fails with line passing through target: expected "+expected+", got "+res)
      coord = Coord(2,0)
      expected = math.sqrt(2)
      res = coord.distFromLine(start, end)
      assert (math.abs(res-expected) < epsilon, "Coord.distFromLine fails with diagonal line: expected "+expected+", got "+res)
      end = Coord(0,2)
      coord = Coord(1)
      expected = 1
      res = coord.distFromLine(start, end)
      assert (res.equals(expected), "Coord.distFromLine fails with vertical line: expected "+expected+", got "+res)
      end = Coord(2,0)
      res = coord.distFromLine(start, end)
      assert (res.equals(expected), "Coord.distFromLine fails with horizontal line: expected "+expected+", got "+res)
      coord = Coord(4,0)
      expected = 2
      res = coord.distFromLine(start, end)
      assert (res.equals(expected), "Coord.distFromLine fails with point after end, on same line: expected "+expected+", got "+res)
      coord = Coord(-3,0)
      expected = 3
      res = coord.distFromLine(start, end)
      assert (res.equals(expected), "Coord.distFromLine fails with point before start, on same line: expected "+expected+", got "+res)
    }

  "Coord.distFromContinuousLine" should "give correct result" in
    {
      val epsilon = 1e-9
      var start = Coord(1, 1)
      var end = Coord(1, 1)
      var coord = Coord(1, 1)
      var expected = 0.0
      var res = coord.distFromContinuousLine(start, end)
      assert (res.equals(expected), "Coord.distFromContinuousLine fails with all in one spot: expected "+expected+", got "+res)
      start = Coord(0.0)
      res = coord.distFromContinuousLine(start, end)
      assert (res.equals(expected), "Coord.distFromContinuousLine fails with end = target: expected "+expected+", got "+res)
      end = Coord(2,2)
      res = coord.distFromContinuousLine(start, end)
      assert (res.equals(expected), "Coord.distFromContinuousLine fails with line passing through target: expected "+expected+", got "+res)
      coord = Coord(2,0)
      expected = math.sqrt(2)
      res = coord.distFromContinuousLine(start, end)
      assert (math.abs(res-expected) < epsilon, "Coord.distFromContinuousLine fails with diagonal line: expected "+expected+", got "+res)
      end = Coord(0,2)
      coord = Coord(1)
      expected = 1
      res = coord.distFromContinuousLine(start, end)
      assert (res.equals(expected), "Coord.distFromContinuousLine fails with vertical line: expected "+expected+", got "+res)
      end = Coord(2,0)
      res = coord.distFromContinuousLine(start, end)
      assert (res.equals(expected), "Coord.distFromContinuousLine fails with horizontal line: expected "+expected+", got "+res)
      coord = Coord(4,0)
      expected = 0
      res = coord.distFromContinuousLine(start, end)
      assert (res.equals(expected), "Coord.distFromContinuousLine fails with point after end, on same line: expected "+expected+", got "+res)
      coord = Coord(-3,0)
      res = coord.distFromContinuousLine(start, end)
      assert (res.equals(expected), "Coord.distFromContinuousLine fails with point before start, on same line: expected "+expected+", got "+res)
    }


  "Coord.toAngle" should "give correct result" in
    {
      val epsilon = 1e-9
      var coord = Coord(100,0)
      assert (math.abs(coord.toAngle - 0) < epsilon, "Coord.toAngle is wrong in one 1,0: "+coord.toAngle)
      coord = Coord(4,4)
      assert (math.abs(coord.toAngle - math.Pi/4) < epsilon, "Coord.toAngle is wrong in 1,1: "+coord.toAngle)
      coord = Coord(0,24)
      assert (math.abs(coord.toAngle - math.Pi/2) < epsilon, "Coord.toAngle is wrong in 0,1: "+coord.toAngle)
      coord= Coord(-5, 5)
      assert (math.abs(coord.toAngle - math.Pi/4*3) < epsilon, "Coord.toAngle wrong in -1,1: "+coord.toAngle)
      coord = Coord(-1, 0)
      assert (math.abs(coord.toAngle - math.Pi) < epsilon, "Coord.toAngle is wrong in -1,0: "+coord.toAngle)
      coord = Coord(-1, -1)
      assert (math.abs(coord.toAngle - math.Pi/4*5) < epsilon, "Coord.toAngle is wrong in -1,-1: "+coord.toAngle)
      coord = Coord(0, -1)
      assert (math.abs(coord.toAngle - math.Pi/2*3) < epsilon, "Coord.toAngle is wrong in 0,-1: "+coord.toAngle)
      coord = Coord(3, -3)
      assert (math.abs(coord.toAngle - math.Pi/4*7) < epsilon, "Coord.toAngle is wrong in 1,-1: "+coord.toAngle)

      coord = Coord(math.sqrt(3), 1)
      assert (math.abs(coord.toAngle - math.Pi/6) < epsilon, "Coord.toAngle is wrong in sqrt3,-1: "+coord.toAngle)
      coord = Coord(math.sqrt(3), -1)
      assert (math.abs(coord.toAngle - math.Pi/6*11) < epsilon, "Coord.toAngle is wrong in sqrt3,-1: "+coord.toAngle)
    }

  "Coord.direction" should "give same result as coord.toAngle when starting at origo" in
    {
      val epsilon = 1e-9
      var coord = Coord(100,0)
      assert (math.abs(Coord(0).direction(coord) - 0) < epsilon, "Coord.direction is wrong in one 1,0: "+Coord(0).direction(coord))
      coord = Coord(4,4)
      assert (math.abs(Coord(0).direction(coord) - math.Pi/4) < epsilon, "Coord.direction is wrong in 1,1: "+Coord(0).direction(coord))
      coord = Coord(0,24)
      assert (math.abs(Coord(0).direction(coord) - math.Pi/2) < epsilon, "Coord.direction is wrong in 0,1: "+Coord(0).direction(coord))
      coord= Coord(-5, 5)
      assert (math.abs(Coord(0).direction(coord) - math.Pi/4*3) < epsilon, "Coord.direction wrong in -1,1: "+Coord(0).direction(coord))
      coord = Coord(-1, 0)
      assert (math.abs(Coord(0).direction(coord) - math.Pi) < epsilon, "Coord.direction is wrong in -1,0: "+Coord(0).direction(coord))
      coord = Coord(-1, -1)
      assert (math.abs(Coord(0).direction(coord) - math.Pi/4*5) < epsilon, "Coord.direction is wrong in -1,-1: "+Coord(0).direction(coord))
      coord = Coord(0, -1)
      assert (math.abs(Coord(0).direction(coord) - math.Pi/2*3) < epsilon, "Coord.direction is wrong in 0,-1: "+Coord(0).direction(coord))
      coord = Coord(3, -3)
      assert (math.abs(Coord(0).direction(coord) - math.Pi/4*7) < epsilon, "Coord.direction is wrong in 1,-1: "+Coord(0).direction(coord))

      coord = Coord(math.sqrt(3), 1)
      assert (math.abs(Coord(0).direction(coord) - math.Pi/6) < epsilon, "Coord.direction is wrong in sqrt3,-1: "+Coord(0).direction(coord))
      coord = Coord(math.sqrt(3), -1)
      assert (math.abs(Coord(0).direction(coord) - math.Pi/6*11) < epsilon, "Coord.direction is wrong in sqrt3,-1: "+Coord(0).direction(coord))
    }

  "Coord.direction" should "give correct results" in
    {
      val epsilon = 1e-9
      var coord = Coord(100,0)
      var comparing = Coord(99,0)
      assert (math.abs(comparing.direction(coord) - 0) < epsilon, "Coord.direction is wrong in one 1,0: "+comparing.direction(coord))
      coord = Coord(4,4)
      comparing = Coord(1,1)
      assert (math.abs(comparing.direction(coord) - math.Pi/4) < epsilon, "Coord.direction is wrong in 1,1: "+comparing.direction(coord))
      coord = Coord(1,24)
      comparing = Coord(1,4)
      assert (math.abs(comparing.direction(coord) - math.Pi/2) < epsilon, "Coord.direction is wrong in 0,1: "+comparing.direction(coord))
      coord= Coord(-5, 5)
      comparing = Coord(5, -5)
      assert (math.abs(comparing.direction(coord) - math.Pi/4*3) < epsilon, "Coord.direction wrong in -1,1: "+comparing.direction(coord))
      coord = Coord(-1, 99)
      comparing = Coord(99,99)
      assert (math.abs(comparing.direction(coord) - math.Pi) < epsilon, "Coord.direction is wrong in -1,0: "+comparing.direction(coord))
      coord = Coord(1, 0)
      comparing = Coord(2, 1)
      assert (math.abs(comparing.direction(coord) - math.Pi/4*5) < epsilon, "Coord.direction is wrong in -1,-1: "+comparing.direction(coord))
      coord = Coord(99, -1)
      comparing = Coord(99,0)
      assert (math.abs(comparing.direction(coord) - math.Pi/2*3) < epsilon, "Coord.direction is wrong in 0,-1: "+comparing.direction(coord))
      coord = Coord(10, 21)
      comparing = Coord(7, 24)
      assert (math.abs(comparing.direction(coord) - math.Pi/4*7) < epsilon, "Coord.direction is wrong in 1,-1: "+comparing.direction(coord))

      coord = Coord(math.sqrt(3)+1, 1)
      comparing = Coord(1,0)
      assert (math.abs(comparing.direction(coord) - math.Pi/6) < epsilon, "Coord.direction is wrong in sqrt3,-1: "+comparing.direction(coord))
      coord = Coord(math.sqrt(3), 2)
      comparing = Coord(0,3)
      assert (math.abs(comparing.direction(coord) - math.Pi/6*11) < epsilon, "Coord.direction is wrong in sqrt3,-1: "+comparing.direction(coord))
    }

  "Coord.angleBetweenVector" should "give correct results" in
    {
      val epsilon = 3e-8 // a bit looser epsilon so it lets these pass
      var coord = Coord(100,0)
      var comparing = Coord(100,0)
      assert (math.abs(comparing.angleBetweenVector(coord) - 0) < epsilon, "Coord.angleBetweenVector is wrong in one 1,0: "+comparing.angleBetweenVector(coord))
      coord = Coord(4,4)
      comparing = Coord(1,0)
      assert (math.abs(comparing.angleBetweenVector(coord) - math.Pi/4) < epsilon, "Coord.angleBetweenVector is wrong in 1,1: "+comparing.angleBetweenVector(coord))
      coord = Coord(24,24)
      comparing = Coord(-1, 1)
      assert (math.abs(comparing.angleBetweenVector(coord) - math.Pi/2) < epsilon, "Coord.angleBetweenVector is wrong in 0,1: "+comparing.angleBetweenVector(coord))
      coord= Coord(-5, 5)
      comparing = Coord(11, 0)
      assert (math.abs(comparing.angleBetweenVector(coord) - math.Pi/4*3) < epsilon, "Coord.angleBetweenVector wrong in -1,1: "+comparing.angleBetweenVector(coord))
      comparing = Coord(0, 11)
      assert (math.abs(comparing.angleBetweenVector(coord) - math.Pi/4) < epsilon, "Coord.angleBetweenVector wrong in -1,1: "+comparing.angleBetweenVector(coord))
      comparing = Coord(5, -5)
      assert (math.abs(comparing.angleBetweenVector(coord) - math.Pi) < epsilon, "Coord.angleBetweenVector wrong in -1,1: "+comparing.angleBetweenVector(coord))
      coord = Coord(-1, 99)
      comparing = Coord(-1,99)
      assert (math.abs(comparing.angleBetweenVector(coord) - 0) < epsilon, "Coord.angleBetweenVector is wrong in -1,0: "+comparing.angleBetweenVector(coord))

      coord = Coord(math.sqrt(3), 1)
      comparing = Coord(1,0)
      assert (math.abs(comparing.angleBetweenVector(coord) - math.Pi/6) < epsilon, "Coord.angleBetweenVector is wrong in sqrt3,-1: "+comparing.angleBetweenVector(coord))
      coord = Coord(math.sqrt(3), -1)
      comparing = Coord(math.sqrt(3), 1)
      assert (math.abs(comparing.angleBetweenVector(coord) - math.Pi/6*2) < epsilon, "Coord.angleBetweenVector is wrong in sqrt3,-1: "+comparing.angleBetweenVector(coord))
    }



  "Coord.map" should "give correct result" in
    {
      var coord = Coord(100,-1).map(_*2)
      assert (coord.equals(Coord(200,-2)), "Coord.map is wrong in simple calculation: "+coord)
      coord = Coord(4,3).map(_=>0)
      assert (coord.equals(Coord(0,0)), "Coord.map is wrong in simple calculation: "+coord)
      coord = Coord(7,24).map(x=>x*2-7)
      assert (coord.equals(Coord(7, 41)), "Coord.map is wrong in simple calculation: "+coord)
      coord= Coord(10.0, 5.5).map(x=>x)
      assert (coord.equals(Coord(10.0, 5.5)), "Coord.map wrong in x=>x: "+coord)
      coord = Coord(0).map(x=>math.pow(x,x))
      assert (coord.equals(Coord(1)), "Coord.map is wrong in 0,0: "+coord)
    }

  "Coord.mapWith" should "give correct result" in
    {
      var coord = Coord(100,1).mapWith(_*_, Coord(2, 5))
      assert (coord.equals(Coord(200, 5)), "Coord.mapWith is wrong in simple calculation: "+coord)
      coord = Coord(4,3).mapWith(_+_, Coord(2, 5))
      assert (coord.equals(Coord(6,8)), "Coord.mapWith is wrong in simple calculation: "+coord)
      coord = Coord(7,24).mapWith(_*2 + _* -2, Coord(2, 5))
      assert (coord.equals(Coord(10, 38)), "Coord.mapWith is wrong in simple calculation: "+coord)
      coord= Coord(10.0, 5.5).mapWith((x,y)=>y, Coord(2, 5))
      assert (coord.equals(Coord(2, 5)), "Coord.mapWith wrong in x,y=>y: "+coord)
      coord = Coord(0).mapWith(_*_+2, Coord(2, 5))
      assert (coord.equals(Coord(2)), "Coord.mapWith is wrong in 0,0: "+coord)
    }


}

