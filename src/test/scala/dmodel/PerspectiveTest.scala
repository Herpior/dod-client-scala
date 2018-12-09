package dmodel

//import org.junit.runner.RunWith
//import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

//@RunWith(classOf[JUnitRunner])
class PerspectiveTest extends FlatSpec with Matchers {

  "Perspective.getDisplacement" should "return correct stuff" in
    {
      Perspective.setPrimary(Coord(0,0))
      val epsilon = 1e-9
      var orig = Coord(10.1, 5.5)
      var cursor = Coord(0)
      var disp = Perspective.getDisplacement(orig, cursor)
      var coord = orig + disp
      assert (coord.dist(Coord(0)) < epsilon, "Perspective.getDisplacement fails with direct pointing: "+coord)
      orig = Coord(-10.1, 50.5)
      disp = Perspective.getDisplacement(orig, cursor)
      coord = orig + disp
      assert (coord.dist(Coord(0)) < epsilon, "Perspective.getDisplacement fails with direct pointing: "+coord)
      orig = Coord(50)
      cursor = Coord(10)
      disp = Perspective.getDisplacement(orig, cursor)
      coord = orig + disp
      assert (coord.dist(Coord(10)) < epsilon, "Perspective.getDisplacement fails with direct pointing: "+coord)
      orig = Coord(49)
      cursor = Coord(49-20, 49-21)
      disp = Perspective.getDisplacement(orig, cursor)
      coord = orig + disp
      assert (coord.dist((Coord(49*math.sqrt(2)- 29).sqr/2)^0.5) < epsilon, "Perspective.getDisplacement fails with near pointing: "+coord)
      orig = Coord(10, 10)
      cursor = Coord(6, 7)
      disp = Perspective.getDisplacement(orig, cursor)
      coord = orig + disp
      assert (coord.dist((Coord(orig.length - 5).sqr/2)^0.5) < epsilon, "Perspective.getDisplacement fails with near pointing: "+coord)
    }

}

