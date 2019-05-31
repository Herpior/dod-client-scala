package dmodel

import dmodel.dpart.{BasicLine, BezierLine}
//import org.junit.runner.RunWith
//import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import java.awt.Color

//@RunWith(classOf[JUnitRunner])
class StrokeTest extends FlatSpec with Matchers {

  "BasicLine.toDodJson" should "return correctly formatted string" in {
    Magic.roundingAccuracy = 2
    val stroke = new BasicLine(Color.decode("#ff9900"),5)
    stroke.addCoord(Coord(0,0))
    stroke.addCoord(Coord(1,1))
    stroke.addCoord(Coord(1,11))
    stroke.addCoord(Coord(2,22.5))
    stroke.addCoord(Coord(3,389.5))
    assert (stroke.toDodJson.equals("{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"}"),stroke.toDodJson+" not equal to {\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"}")

    val stroke2 = new BasicLine(Color.decode("#abccee"),5)
    stroke2.addCoord(Coord(0,0))
    stroke2.addCoord(Coord(1,1))
    assert (stroke2.toDodJson.equals("{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"}"),stroke2.toDodJson+" not equal to {\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"}")

    val semitransparent = new Color(0, 0, 0, 64)
    val stroke3 = new BasicLine(semitransparent,5)
    stroke3.addCoord(Coord(0,0))
    stroke3.addCoord(Coord(1,1))
    assert (stroke3.toDodJson.equals("{\"path\":[0,0,1,1],\"size\":5,\"color\":\"rgba(0,0,0,0.251)\"}"),stroke3.toDodJson+" was not the expected value")
  }


  "BezierLine.toDodJson" should "return end points in correct accuracy" in {
    Magic.roundingAccuracy = 2
    val stroke = new BezierLine(Color.decode("#ff9900"),5)
    stroke.setCoords(Array(Coord(0.2,-0.22), Coord(0), Coord(5), Coord(1.314, 10.7865375)))
    val coords = stroke.getLines.head.getCoords
    assert(coords.head.equals(Coord(0,0)), "first coordinate wrong: "+coords.head)
    assert(coords.last.equals(Coord(1.5,11)), "last coordinate wrong: "+coords.last)

    Magic.roundingAccuracy = 10
    val stroke10 = new BezierLine(Color.decode("#ff9900"),5)
    stroke10.setCoords(Array(Coord(0.2,-0.22), Coord(0), Coord(5), Coord(1.314, 10.7865375)))
    val coords10 = stroke10.getLines.head.getCoords
    assert(coords10.head.equals(Coord(0.2,-0.2)), "first coordinate wrong: "+coords10.head)
    assert(coords10.last.equals(Coord(1.3,10.8)), "last coordinate wrong: "+coords10.last)
  }

}

