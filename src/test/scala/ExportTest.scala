package tests

import dmodel.dpart.BasicLine
import org.scalatest.FlatSpec
import org.scalatest.Matchers
//import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.awt.Color
import dmodel._

//@RunWith(classOf[JUnitRunner])
class ExportTest extends FlatSpec with Matchers {

  def fixture =
    new {
      val doodleModel = new DoodleModel
    }

  "DoodleModel.toDodPostJsonStrokes" should "return correctly formatted string" in
    {
      val f = fixture
      val stroke = new BasicLine(Color.decode("#ff9900"),5)
      stroke.addCoord(Coord(0,0))
      stroke.addCoord(Coord(1,1))
      stroke.addCoord(Coord(1,11))
      stroke.addCoord(Coord(2,22.5))
      stroke.addCoord(Coord(3,389.5))

      val stroke2 = new BasicLine(Color.decode("#abccee"),5)
      stroke2.addCoord(Coord(0,0))
      stroke2.addCoord(Coord(1,1))

      val editStroke = new EditLine(stroke, stroke)
      
      val semitransparent = new Color(0, 0, 0, 64)
      val stroke3 = new BasicLine(semitransparent,5)
      stroke3.addCoord(Coord(0,0))
      stroke3.addCoord(Coord(1,1))


      f.doodleModel.layers.getCurrent.add(stroke)
      assert (f.doodleModel.toDodPostJsonStrokes.equals("[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"}]"), "doodle with one BasicLine not correctly formatted when posting it")
      f.doodleModel.layers.getCurrent.add(stroke2)
      assert (f.doodleModel.toDodPostJsonStrokes.equals("[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"},{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"}]"), "doodle with two BasicLines not correctly formatted when posting it")
      f.doodleModel.layers.getCurrent.add(editStroke)
      assert (f.doodleModel.toDodPostJsonStrokes.equals("[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"},{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"}]"), "doodle with EditLine not correctly formatted when posting it")
      f.doodleModel.layers.getCurrent.add(stroke3)
      assert (f.doodleModel.toDodPostJsonStrokes.equals("[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"},{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"},{\"path\":[0,0,1,1],\"size\":5,\"color\":\"rgba(0,0,0,0.251)\"}]"), "doodle with semitransparent line not correctly formatted when posting it")

    }

}

