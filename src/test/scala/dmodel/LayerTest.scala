package dmodel

import dmodel.dpart.{BasicLine, BezierLine, EditLine}
//import org.junit.runner.RunWith
//import org.scalatest.junit.JUnitRunner
import java.awt.Color

import org.scalatest.{FlatSpec, Matchers}

//@RunWith(classOf[JUnitRunner])
class LayerTest extends FlatSpec with Matchers {

  "Layer.split" should "work properly with editlines" in {
    val model = new DoodleModel
    val layer = model.layers.getCurrent

    val stroke = new BasicLine(Color.decode("#ff9900"),5)
    stroke.addCoord(Coord(0,0))
    stroke.addCoord(Coord(1,1))
    stroke.addCoord(Coord(1,11))
    stroke.addCoord(Coord(2,22.5))
    stroke.addCoord(Coord(3,389.5))

    val stroke2 = new BasicLine(Color.decode("#abccee"),5)
    stroke2.addCoord(Coord(0,0))
    stroke2.addCoord(Coord(1,1))

    val semitransparent = new Color(0, 0, 0, 64)
    val stroke3 = new BasicLine(semitransparent,5)
    stroke3.addCoord(Coord(0,0))
    stroke3.addCoord(Coord(1,1))

    val editline = new EditLine(stroke2, stroke)
    val editline2 = new EditLine(stroke3, stroke2)

    layer.add(stroke3)
    layer.add(editline)
    layer.add(editline2)
    assert(layer.getStrokes(false).deep == Array(stroke3, editline, editline2).deep, "layer's strokes are wrong before doing anything")
    layer.undo
    assert(layer.getStrokes(false).deep == Array(stroke2, editline).deep, "layer's strokes are wrong after one undo")
    assert(layer.getRedos.deep == Array(editline2).deep, "layer's redos are wrong after one undo")
    layer.undo
    assert(layer.getStrokes(false).deep == Array(stroke).deep, "layer's strokes are wrong after two undos")
    assert(layer.getRedos.deep == Array(editline2, editline).deep, "layer's redos are wrong after two undos")
    layer.undo
    assert(layer.getStrokes(false).deep == Array().deep, "layer's strokes are wrong after three undos")
    assert(layer.getRedos.deep == Array(editline2, editline, stroke).deep, "layer's redos are wrong after three undos")

    val splitted = layer.split
    assert(layer.getStrokes(false).deep == Array().deep, "layer's strokes are wrong after split")
    assert(layer.getRedos.deep == Array().deep, "layer's redos are wrong after split")
    assert(splitted.getStrokes(false).deep == Array(stroke3, editline, editline2).deep, "split layer's strokes are wrong after split")
    assert(splitted.getRedos.deep == Array().deep, "split layer's redos are wrong after split")
    splitted.undo
    splitted.undo
    val splitted2 = splitted.split
    assert(splitted.getStrokes(false).deep == Array(stroke3, editline, editline2).deep, "layer's strokes are wrong after split")
    assert(splitted.getRedos.deep == Array().deep, "layer's redos are wrong after split")
    assert(splitted2.getStrokes(false).deep == Array().deep, "split layer's strokes are wrong after split")
    assert(splitted2.getRedos.deep == Array().deep, "split layer's redos are wrong after split")
  }

}

