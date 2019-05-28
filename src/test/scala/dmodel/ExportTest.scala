package dmodel

import java.awt.Color

import dmodel.dpart.{BasicLine, BezierLine, EditLine, MultiLine}
import dmodel.json.JsonParse
//import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
//import org.scalatest.junit.JUnitRunner

//@RunWith(classOf[JUnitRunner])
class ExportTest extends FlatSpec with Matchers {

  "DoodleModel.toDodPostJsonStrokes" should "return correctly formatted string for uploading to dod" in {
    val doodleModel = new DoodleModel
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
    val stroke3 = new BasicLine(semitransparent,50)
    stroke3.addCoord(Coord(0,0))
    stroke3.addCoord(Coord(1,1))


    doodleModel.layers.getCurrent.add(stroke)
    assert (doodleModel.toDodPostJsonStrokes.equals("[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"}]"), "doodle with one BasicLine not correctly formatted when posting it")
    doodleModel.layers.getCurrent.add(stroke2)
    assert (doodleModel.toDodPostJsonStrokes.equals("[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"},{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"}]"), "doodle with two BasicLines not correctly formatted when posting it")
    doodleModel.layers.getCurrent.add(editStroke)
    assert (doodleModel.toDodPostJsonStrokes.equals("[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"},{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"}]"), "doodle with EditLine not correctly formatted when posting it")
    doodleModel.layers.getCurrent.add(stroke3)
    assert (doodleModel.toDodPostJsonStrokes.equals("[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"},{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"},{\"path\":[0,0,1,1],\"size\":50,\"color\":\"rgba(0,0,0,0.251)\"}]"), "doodle with semitransparent line not correctly formatted when posting it")

  }
  "DoodleModel.toDodPostJsonStrokes" should "return correctly rounded string with 0.5 accuracy" in {
    val doodleModel = new DoodleModel
    Magic.roundingAccuracy = 2

    val stroke = new BasicLine(Color.decode("#ff9900"),5)
    stroke.addCoord(Coord(0,0.1))
    stroke.addCoord(Coord(1,1.11))
    stroke.addCoord(Coord(1,11.111))
    stroke.addCoord(Coord(2,22.3))
    stroke.addCoord(Coord(2.95,389.5))

    val stroke2 = new BasicLine(Color.decode("#abccee"),5)
    stroke2.addCoord(Coord(-0.2,0.2))
    stroke2.addCoord(Coord(0.9,0.896427658756378))

    val editStroke = new EditLine(stroke, stroke)

    val semitransparent = new Color(0, 0, 0, 64)
    val stroke3 = new BasicLine(semitransparent,50)
    stroke3.addCoord(Coord(-0.2,0.2))
    stroke3.addCoord(Coord(0.9,0.896427658756378))

    doodleModel.layers.getCurrent.add(stroke)
    var expected = "[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"}]"
    var res = doodleModel.toDodPostJsonStrokes
    assert (res.equals(expected), "doodle with one BasicLine not correctly rounded: \nexpected "+expected+",\ngot      "+res)
    doodleModel.layers.getCurrent.add(stroke2)
    expected = "[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"},{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"}]"
    res = doodleModel.toDodPostJsonStrokes
    assert (doodleModel.toDodPostJsonStrokes.equals(expected), "doodle with two BasicLines not correctly rounded: \nexpected "+expected+",\ngot      "+res)
    doodleModel.layers.getCurrent.add(editStroke)
    res = doodleModel.toDodPostJsonStrokes
    assert (doodleModel.toDodPostJsonStrokes.equals(expected), "doodle with EditLine not correctly rounded: \nexpected "+expected+",\ngot      "+res)
    doodleModel.layers.getCurrent.add(stroke3)
    expected = "[{\"path\":[0,0,1,1,1,11,2,22.5,3,389.5],\"size\":5,\"color\":\"#f90\"},{\"path\":[0,0,1,1],\"size\":5,\"color\":\"#abccee\"},{\"path\":[0,0,1,1],\"size\":50,\"color\":\"rgba(0,0,0,0.251)\"}]"
    res = doodleModel.toDodPostJsonStrokes
    assert (doodleModel.toDodPostJsonStrokes.equals(expected), "doodle with semitransparent line not correctly rounded: \nexpected "+expected+",\ngot      "+res)

  }
  "DoodleModel.toDodPostJsonStrokes" should "return correctly rounded string with 0.1 accuracy" in {
    val doodleModel = new DoodleModel
    Magic.roundingAccuracy = 10

    val stroke = new BasicLine(Color.decode("#ff9900"),5)
    stroke.addCoord(Coord(0,0.1))
    stroke.addCoord(Coord(1,1.11))
    stroke.addCoord(Coord(1,11.111))
    stroke.addCoord(Coord(2,22.3))
    stroke.addCoord(Coord(2.95,389.5))

    val stroke2 = new BasicLine(Color.decode("#abccee"),5)
    stroke2.addCoord(Coord(-0.2,0.2))
    stroke2.addCoord(Coord(0.9,0.896427658756378))

    val editStroke = new EditLine(stroke, stroke)

    val semitransparent = new Color(0, 0, 0, 64)
    val stroke3 = new BasicLine(semitransparent,50)
    stroke3.addCoord(Coord(-0.2,0.2))
    stroke3.addCoord(Coord(0.9,0.896427658756378))

    doodleModel.layers.getCurrent.add(stroke)
    val exp_stroke = "{\"path\":[0,0.1,1,1.1,1,11.1,2,22.3,3,389.5],\"size\":5,\"color\":\"#f90\"}"
    var expected = "["+exp_stroke+"]"
    var res = doodleModel.toDodPostJsonStrokes
    assert (res.equals(expected), "doodle with one BasicLine not correctly rounded: \nexpected "+expected+",\ngot      "+res)
    doodleModel.layers.getCurrent.add(stroke2)
    val exp_stroke2 = "{\"path\":[-0.2,0.2,0.9,0.9],\"size\":5,\"color\":\"#abccee\"}"
    expected = "["+exp_stroke+","+exp_stroke2+"]"
    res = doodleModel.toDodPostJsonStrokes
    assert (doodleModel.toDodPostJsonStrokes.equals(expected), "doodle with two BasicLines not correctly rounded: \nexpected "+expected+",\ngot      "+res)
    doodleModel.layers.getCurrent.add(editStroke)
    res = doodleModel.toDodPostJsonStrokes
    assert (doodleModel.toDodPostJsonStrokes.equals(expected), "doodle with EditLine not correctly rounded: \nexpected "+expected+",\ngot      "+res)
    doodleModel.layers.getCurrent.add(stroke3)
    val exp_stroke3 = "{\"path\":[-0.2,0.2,0.9,0.9],\"size\":50,\"color\":\"rgba(0,0,0,0.251)\"}"
    expected = "["+exp_stroke+","+exp_stroke2+","+exp_stroke3+"]"
    res = doodleModel.toDodPostJsonStrokes
    assert (doodleModel.toDodPostJsonStrokes.equals(expected), "doodle with semitransparent line not correctly rounded: \nexpected "+expected+",\ngot      "+res)

  }

  "LayerList.toJsonString" should "return correctly formatted string for saving as json" in {
    val doodleModel = new DoodleModel
    val layerList = doodleModel.layers

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
    val stroke3 = new BasicLine(semitransparent,50)
    stroke3.addCoord(Coord(0,0))
    stroke3.addCoord(Coord(1,1))

    val bezLine = new BezierLine(new Color(255, 255, 255, 68), 10)
    bezLine.setCoords(Array(Coord(0), Coord(1), Coord(2,22.5),Coord(3,389.5)))

    val multi = new MultiLine
    val dot = new BasicLine(new Color(0, 0, 255), 1)
    dot.addCoord(Coord(55))
    val dot2 = new BasicLine(new Color(255, 0, 0), 2)
    dot2.addCoord(Coord(300))
    multi.addLine(dot)
    multi.addLine(dot2)


    val chain = "chain name"
    def expected_start(t:Int) = {
      val ver = view.DoodleWindow.version
      val di = chain
      "{\"version\":"+ver+",\"doodle_id\":\""+di+"\",\"time\":"+t+",\"layers\":["
    }

    doodleModel.layers.getCurrent.add(stroke)
    val expected_stroke = "{\"linetype\":\"basic\",\"color\":\"#f90\",\"size\":5,\"path\":[0,0,1,1,1,11,2,22.5,3,389.5]}"
    var expected = "{\"version\":499,\"doodle_id\":\""+chain+"\",\"time\":1,\"layers\":[{\"strokes\":["+expected_stroke+"],\"visible\":true}]}"
    var testing = layerList.toJsonString(1, chain)
    assert (testing.equals(expected), "doodle with one BasicLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layerList.getCurrent.add(stroke2)
    var time = 123
    val expected_stroke2 = "{\"linetype\":\"basic\",\"color\":\"#abccee\",\"size\":5,\"path\":[0,0,1,1]}"
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+"],\"visible\":true}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two BasicLines not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 2562
    layerList.getCurrent.add(editStroke)
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+"],\"visible\":true}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with EditLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 34526
    layerList.getCurrent.add(stroke3)
    val expected_stroke3 = "{\"linetype\":\"basic\",\"color\":\"#00000040\",\"size\":50,\"path\":[0,0,1,1]}"
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with semitransparent line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    val layer2 = new Layer
    layerList.addLayer(layer2)
    layer2.setVisibility(false)
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":[],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(stroke2)
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":["+expected_stroke2+"],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)
    expected = expected_start(time)+"{\"strokes\":["+stroke.toJsonString.get+","+stroke2.toJsonString.get+","+stroke3.toJsonString.get+"],\"visible\":true},{\"strokes\":["+stroke2.toJsonString.get+"],\"visible\":false}]}"
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(bezLine)
    val expected_bezier = "{\"linetype\":\"bezier\",\"color\":\"#ffffff44\",\"size\":10,\"coords\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":1},{\"x\":2,\"y\":22.5},{\"x\":3,\"y\":389.5}]}"//consider changing the format to a cleaner one in this mode too?
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":["+expected_stroke2+","+expected_bezier+"],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with bezier line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(multi)
    val exp_dot = "{\"linetype\":\"basic\",\"color\":\"#00f\",\"size\":1,\"path\":[55,55]}"
    val exp_dot2 = "{\"linetype\":\"basic\",\"color\":\"#f00\",\"size\":2,\"path\":[300,300]}"
    val expected_multi = "{\"linetype\":\"multi\",\"strokes\":["+exp_dot+","+exp_dot2+"]}"
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":["+expected_stroke2+","+expected_bezier+","+expected_multi+"],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with multi line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

  }
  "LayerList.toJsonString" should "return correctly rounded string with 0.5 accuracy" in {
    val doodleModel = new DoodleModel
    val layerList = doodleModel.layers
    Magic.roundingAccuracy = 2

    val stroke = new BasicLine(Color.decode("#ff9900"),5)
    stroke.addCoord(Coord(0,0.1))
    stroke.addCoord(Coord(1,1.11))
    stroke.addCoord(Coord(1,11.111))
    stroke.addCoord(Coord(2,22.3))
    stroke.addCoord(Coord(2.95,389.5))

    val stroke2 = new BasicLine(Color.decode("#abccee"),5)
    stroke2.addCoord(Coord(-0.2,0.2))
    stroke2.addCoord(Coord(0.9,0.896427658756378))

    val editStroke = new EditLine(stroke, stroke)

    val semitransparent = new Color(0, 0, 0, 64)
    val stroke3 = new BasicLine(semitransparent,50)
    stroke3.addCoord(Coord(-0.2,0.2))
    stroke3.addCoord(Coord(0.9,0.896427658756378))

    val bezLine = new BezierLine(new Color(255, 255, 255, 68), 10)
    bezLine.setCoords(Array(Coord(-0.24), Coord(1.112058), Coord(2,22.53525),Coord(2.854,389.5)))

    val multi = new MultiLine
    val dot = new BasicLine(new Color(0, 0, 255), 1)
    dot.addCoord(Coord(55))
    val dot2 = new BasicLine(new Color(255, 0, 0), 2)
    dot2.addCoord(Coord(300))
    multi.addLine(dot)
    multi.addLine(dot2)


    val chain = "chain name"
    def expected_start(t:Int) = {
      val ver = view.DoodleWindow.version
      val di = chain
      "{\"version\":"+ver+",\"doodle_id\":\""+di+"\",\"time\":"+t+",\"layers\":["
    }

    doodleModel.layers.getCurrent.add(stroke)
    val expected_stroke = "{\"linetype\":\"basic\",\"color\":\"#f90\",\"size\":5,\"path\":[0,0,1,1,1,11,2,22.5,3,389.5]}"
    var expected = "{\"version\":499,\"doodle_id\":\""+chain+"\",\"time\":1,\"layers\":[{\"strokes\":["+expected_stroke+"],\"visible\":true}]}"
    var testing = layerList.toJsonString(1, chain)
    assert (testing.equals(expected), "doodle with one BasicLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layerList.getCurrent.add(stroke2)
    var time = 123
    val expected_stroke2 = "{\"linetype\":\"basic\",\"color\":\"#abccee\",\"size\":5,\"path\":[0,0,1,1]}"
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+"],\"visible\":true}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two BasicLines not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 2562
    layerList.getCurrent.add(editStroke)
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+"],\"visible\":true}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with EditLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 34526
    layerList.getCurrent.add(stroke3)
    val expected_stroke3 = "{\"linetype\":\"basic\",\"color\":\"#00000040\",\"size\":50,\"path\":[0,0,1,1]}"
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with semitransparent line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    val layer2 = new Layer
    layerList.addLayer(layer2)
    layer2.setVisibility(false)
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":[],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(stroke2)
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":["+expected_stroke2+"],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)
    expected = expected_start(time)+"{\"strokes\":["+stroke.toJsonString.get+","+stroke2.toJsonString.get+","+stroke3.toJsonString.get+"],\"visible\":true},{\"strokes\":["+stroke2.toJsonString.get+"],\"visible\":false}]}"
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(bezLine)
    val expected_bezier = "{\"linetype\":\"bezier\",\"color\":\"#ffffff44\",\"size\":10,\"coords\":[{\"x\":-0.24,\"y\":-0.24},{\"x\":1.112058,\"y\":1.112058},{\"x\":2,\"y\":22.53525},{\"x\":2.854,\"y\":389.5}]}" //bez lines keep full accuracy because idk.. maybe restricting it to 3-4 decimals could be ok, but at that point it's not going to save much...
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":["+expected_stroke2+","+expected_bezier+"],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with bezier line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(multi)
    val exp_dot = "{\"linetype\":\"basic\",\"color\":\"#00f\",\"size\":1,\"path\":[55,55]}"
    val exp_dot2 = "{\"linetype\":\"basic\",\"color\":\"#f00\",\"size\":2,\"path\":[300,300]}"
    val expected_multi = "{\"linetype\":\"multi\",\"strokes\":["+exp_dot+","+exp_dot2+"]}"
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":["+expected_stroke2+","+expected_bezier+","+expected_multi+"],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with multi line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

  }
  "LayerList.toJsonString" should "return correctly rounded string with 0.1 accuracy" in {
    val doodleModel = new DoodleModel
    val layerList = doodleModel.layers
    Magic.roundingAccuracy = 10

    val stroke = new BasicLine(Color.decode("#ff9900"),5)
    stroke.addCoord(Coord(0,0.1))
    stroke.addCoord(Coord(1,1.11))
    stroke.addCoord(Coord(1,11.111))
    stroke.addCoord(Coord(2,22.3))
    stroke.addCoord(Coord(2.95,389.5))

    val stroke2 = new BasicLine(Color.decode("#abccee"),5)
    stroke2.addCoord(Coord(-0.2,0.2))
    stroke2.addCoord(Coord(0.9,0.896427658756378))

    val editStroke = new EditLine(stroke, stroke)

    val semitransparent = new Color(0, 0, 0, 64)
    val stroke3 = new BasicLine(semitransparent,50)
    stroke3.addCoord(Coord(-0.2,0.2))
    stroke3.addCoord(Coord(0.9,0.896427658756378))

    val bezLine = new BezierLine(new Color(255, 255, 255, 68), 10)
    bezLine.setCoords(Array(Coord(-0.24), Coord(1.112058), Coord(2,22.53525),Coord(2.854,389.5)))

    val multi = new MultiLine
    val dot = new BasicLine(new Color(0, 0, 255), 1)
    dot.addCoord(Coord(55))
    val dot2 = new BasicLine(new Color(255, 0, 0), 2)
    dot2.addCoord(Coord(300))
    multi.addLine(dot)
    multi.addLine(dot2)


    val chain = "chain name"
    def expected_start(t:Int) = {
      val ver = view.DoodleWindow.version
      val di = chain
      "{\"version\":"+ver+",\"doodle_id\":\""+di+"\",\"time\":"+t+",\"layers\":["
    }

    doodleModel.layers.getCurrent.add(stroke)
    val expected_stroke = "{\"linetype\":\"basic\",\"color\":\"#f90\",\"size\":5,\"path\":[0,0.1,1,1.1,1,11.1,2,22.3,3,389.5]}"
    var expected = "{\"version\":499,\"doodle_id\":\""+chain+"\",\"time\":1,\"layers\":[{\"strokes\":["+expected_stroke+"],\"visible\":true}]}"
    var testing = layerList.toJsonString(1, chain)
    assert (testing.equals(expected), "doodle with one BasicLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layerList.getCurrent.add(stroke2)
    var time = 123
    val expected_stroke2 = "{\"linetype\":\"basic\",\"color\":\"#abccee\",\"size\":5,\"path\":[-0.2,0.2,0.9,0.9]}"
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+"],\"visible\":true}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two BasicLines not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 2562
    layerList.getCurrent.add(editStroke)
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+"],\"visible\":true}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with EditLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 34526
    layerList.getCurrent.add(stroke3)
    val expected_stroke3 = "{\"linetype\":\"basic\",\"color\":\"#00000040\",\"size\":50,\"path\":[-0.2,0.2,0.9,0.9]}"
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with semitransparent line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    val layer2 = new Layer
    layerList.addLayer(layer2)
    layer2.setVisibility(false)
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":[],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(stroke2)
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":["+expected_stroke2+"],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)
    expected = expected_start(time)+"{\"strokes\":["+stroke.toJsonString.get+","+stroke2.toJsonString.get+","+stroke3.toJsonString.get+"],\"visible\":true},{\"strokes\":["+stroke2.toJsonString.get+"],\"visible\":false}]}"
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(bezLine)
    val expected_bezier = "{\"linetype\":\"bezier\",\"color\":\"#ffffff44\",\"size\":10,\"coords\":[{\"x\":-0.24,\"y\":-0.24},{\"x\":1.112058,\"y\":1.112058},{\"x\":2,\"y\":22.53525},{\"x\":2.854,\"y\":389.5}]}" //bez lines keep full accuracy because idk.. maybe restricting it to 3-4 decimals could be ok, but at that point it's not going to save much...
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":["+expected_stroke2+","+expected_bezier+"],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with bezier line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(multi)
    val exp_dot = "{\"linetype\":\"basic\",\"color\":\"#00f\",\"size\":1,\"path\":[55,55]}"
    val exp_dot2 = "{\"linetype\":\"basic\",\"color\":\"#f00\",\"size\":2,\"path\":[300,300]}"
    val expected_multi = "{\"linetype\":\"multi\",\"strokes\":["+exp_dot+","+exp_dot2+"]}"
    expected = expected_start(time)+"{\"strokes\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"visible\":true},{\"strokes\":["+expected_stroke2+","+expected_bezier+","+expected_multi+"],\"visible\":false}]}"
    testing = layerList.toJsonString(time, chain)
    assert (testing.equals(expected), "doodle with multi line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

  }

  "LayerList.toShortJsonString" should "return correctly formatted string for saving as json" in {
    val doodleModel = new DoodleModel
    val layerList = doodleModel.layers

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
    val stroke3 = new BasicLine(semitransparent,50)
    stroke3.addCoord(Coord(0,0))
    stroke3.addCoord(Coord(1,1))

    val bezLine = new BezierLine(new Color(255, 255, 255, 68), 10)
    bezLine.setCoords(Array(Coord(0), Coord(1), Coord(2,22.5),Coord(3,389.5)))

    val multi = new MultiLine
    val dot = new BasicLine(new Color(0, 0, 255), 1)
    dot.addCoord(Coord(55))
    val dot2 = new BasicLine(new Color(255, 0, 0), 2)
    dot2.addCoord(Coord(300))
    multi.addLine(dot)
    multi.addLine(dot2)


    val chain = "chain name"
    def expected_start(t:Int) = {
      val ver = view.DoodleWindow.version
      val di = chain
      "{\"v\":"+ver+",\"d\":\""+di+"\",\"t\":"+t+",\"l\":["
    }

    doodleModel.layers.getCurrent.add(stroke)
    val expected_stroke = "{\"l\":\"n\",\"c\":\"#f90\",\"s\":5,\"p\":[0,0,1,1,1,11,2,22.5,3,389.5]}"
    var expected = "{\"v\":499,\"d\":\""+chain+"\",\"t\":1,\"l\":[{\"s\":["+expected_stroke+"],\"v\":true}]}"
    var testing = layerList.toShortJsonString(1,chain)
    assert (testing.equals(expected), "doodle with one BasicLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layerList.getCurrent.add(stroke2)
    var time = 123
    val expected_stroke2 = "{\"l\":\"n\",\"c\":\"#abccee\",\"s\":5,\"p\":[0,0,1,1]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+"],\"v\":true}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two BasicLines not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 2562
    layerList.getCurrent.add(editStroke)
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+"],\"v\":true}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with EditLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 34526
    layerList.getCurrent.add(stroke3)
    val expected_stroke3 = "{\"l\":\"n\",\"c\":\"#00000040\",\"s\":50,\"p\":[0,0,1,1]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with semitransparent line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    val layer2 = new Layer
    layerList.addLayer(layer2)
    layer2.setVisibility(false)
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":[],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(stroke2)
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":["+expected_stroke2+"],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)
    expected = expected_start(time)+"{\"s\":["+stroke.toShortJsonString.get+","+stroke2.toShortJsonString.get+","+stroke3.toShortJsonString.get+"],\"v\":true},{\"s\":["+stroke2.toShortJsonString.get+"],\"v\":false}]}"
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(bezLine)
    val expected_bezier = "{\"l\":\"b\",\"c\":\"#ffffff44\",\"s\":10,\"p\":[0,0,1,1,2,22.5,3,389.5]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":["+expected_stroke2+","+expected_bezier+"],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with bezier line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(multi)
    val exp_dot = "{\"l\":\"n\",\"c\":\"#00f\",\"s\":1,\"p\":[55,55]}"
    val exp_dot2 = "{\"l\":\"n\",\"c\":\"#f00\",\"s\":2,\"p\":[300,300]}"
    val expected_multi = "{\"l\":\"m\",\"ss\":["+exp_dot+","+exp_dot2+"]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":["+expected_stroke2+","+expected_bezier+","+expected_multi+"],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with multi line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

  }
  "LayerList.toShortJsonString" should "return correctly rounded string with 0.5 accuracy" in {
    val doodleModel = new DoodleModel
    val layerList = doodleModel.layers
    Magic.roundingAccuracy = 2

    val stroke = new BasicLine(Color.decode("#ff9900"),5)
    stroke.addCoord(Coord(0,0.1))
    stroke.addCoord(Coord(1,1.11))
    stroke.addCoord(Coord(1,11.111))
    stroke.addCoord(Coord(2,22.3))
    stroke.addCoord(Coord(2.95,389.5))

    val stroke2 = new BasicLine(Color.decode("#abccee"),5)
    stroke2.addCoord(Coord(-0.2,0.2))
    stroke2.addCoord(Coord(0.9,0.896427658756378))

    val editStroke = new EditLine(stroke, stroke)

    val semitransparent = new Color(0, 0, 0, 64)
    val stroke3 = new BasicLine(semitransparent,50)
    stroke3.addCoord(Coord(-0.2,0.2))
    stroke3.addCoord(Coord(0.9,0.896427658756378))

    val bezLine = new BezierLine(new Color(255, 255, 255, 68), 10)
    bezLine.setCoords(Array(Coord(-0.24), Coord(1.112058), Coord(2,22.53525),Coord(2.854,389.5)))

    val multi = new MultiLine
    val dot = new BasicLine(new Color(0, 0, 255), 1)
    dot.addCoord(Coord(55))
    val dot2 = new BasicLine(new Color(255, 0, 0), 2)
    dot2.addCoord(Coord(300))
    multi.addLine(dot)
    multi.addLine(dot2)

    val chain = "chain name"
    def expected_start(t:Int) = {
      val ver = view.DoodleWindow.version
      val di = chain
      "{\"v\":"+ver+",\"d\":\""+di+"\",\"t\":"+t+",\"l\":["
    }

    doodleModel.layers.getCurrent.add(stroke)
    val expected_stroke = "{\"l\":\"n\",\"c\":\"#f90\",\"s\":5,\"p\":[0,0,1,1,1,11,2,22.5,3,389.5]}"
    var expected = "{\"v\":499,\"d\":\""+chain+"\",\"t\":1,\"l\":[{\"s\":["+expected_stroke+"],\"v\":true}]}"
    var testing = layerList.toShortJsonString(1,chain)
    assert (testing.equals(expected), "doodle with one BasicLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layerList.getCurrent.add(stroke2)
    var time = 123
    val expected_stroke2 = "{\"l\":\"n\",\"c\":\"#abccee\",\"s\":5,\"p\":[0,0,1,1]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+"],\"v\":true}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two BasicLines not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 2562
    layerList.getCurrent.add(editStroke)
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+"],\"v\":true}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with EditLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 34526
    layerList.getCurrent.add(stroke3)
    val expected_stroke3 = "{\"l\":\"n\",\"c\":\"#00000040\",\"s\":50,\"p\":[0,0,1,1]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with semitransparent line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    val layer2 = new Layer
    layerList.addLayer(layer2)
    layer2.setVisibility(false)
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":[],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(stroke2)
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":["+expected_stroke2+"],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)
    expected = expected_start(time)+"{\"s\":["+stroke.toShortJsonString.get+","+stroke2.toShortJsonString.get+","+stroke3.toShortJsonString.get+"],\"v\":true},{\"s\":["+stroke2.toShortJsonString.get+"],\"v\":false}]}"
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(bezLine)
    val expected_bezier = "{\"l\":\"b\",\"c\":\"#ffffff44\",\"s\":10,\"p\":[-0.24,-0.24,1.112058,1.112058,2,22.53525,2.854,389.5]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":["+expected_stroke2+","+expected_bezier+"],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with bezier line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(multi)
    val exp_dot = "{\"l\":\"n\",\"c\":\"#00f\",\"s\":1,\"p\":[55,55]}"
    val exp_dot2 = "{\"l\":\"n\",\"c\":\"#f00\",\"s\":2,\"p\":[300,300]}"
    val expected_multi = "{\"l\":\"m\",\"ss\":["+exp_dot+","+exp_dot2+"]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":["+expected_stroke2+","+expected_bezier+","+expected_multi+"],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with multi line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

  }
  "LayerList.toShortJsonString" should "return correctly rounded string with 0.1 accuracy" in {
    val doodleModel = new DoodleModel
    val layerList = doodleModel.layers
    Magic.roundingAccuracy = 10

    val stroke = new BasicLine(Color.decode("#ff9900"),5)
    stroke.addCoord(Coord(0,0.1))
    stroke.addCoord(Coord(1,1.11))
    stroke.addCoord(Coord(1,11.111))
    stroke.addCoord(Coord(2,22.3))
    stroke.addCoord(Coord(2.95,389.5))

    val stroke2 = new BasicLine(Color.decode("#abccee"),5)
    stroke2.addCoord(Coord(-0.2,0.2))
    stroke2.addCoord(Coord(0.9,0.896427658756378))

    val editStroke = new EditLine(stroke, stroke)

    val semitransparent = new Color(0, 0, 0, 64)
    val stroke3 = new BasicLine(semitransparent,50)
    stroke3.addCoord(Coord(-0.2,0.2))
    stroke3.addCoord(Coord(0.9,0.896427658756378))

    val bezLine = new BezierLine(new Color(255, 255, 255, 68), 10)
    bezLine.setCoords(Array(Coord(-0.24), Coord(1.112058), Coord(2,22.53525),Coord(2.854,389.5)))

    val multi = new MultiLine
    val dot = new BasicLine(new Color(0, 0, 255), 1)
    dot.addCoord(Coord(55))
    val dot2 = new BasicLine(new Color(255, 0, 0), 2)
    dot2.addCoord(Coord(300))
    multi.addLine(dot)
    multi.addLine(dot2)

    val chain = "chain name"
    def expected_start(t:Int) = {
      val ver = view.DoodleWindow.version
      val di = chain
      "{\"v\":"+ver+",\"d\":\""+di+"\",\"t\":"+t+",\"l\":["
    }

    doodleModel.layers.getCurrent.add(stroke)
    val expected_stroke = "{\"l\":\"n\",\"c\":\"#f90\",\"s\":5,\"p\":[0,0.1,1,1.1,1,11.1,2,22.3,3,389.5]}"
    var expected = "{\"v\":499,\"d\":\""+chain+"\",\"t\":1,\"l\":[{\"s\":["+expected_stroke+"],\"v\":true}]}"
    var testing = layerList.toShortJsonString(1,chain)
    assert (testing.equals(expected), "doodle with one BasicLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layerList.getCurrent.add(stroke2)
    var time = 123
    val expected_stroke2 = "{\"l\":\"n\",\"c\":\"#abccee\",\"s\":5,\"p\":[-0.2,0.2,0.9,0.9]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+"],\"v\":true}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two BasicLines not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 2562
    layerList.getCurrent.add(editStroke)
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+"],\"v\":true}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with EditLine not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    time = 34526
    layerList.getCurrent.add(stroke3)
    val expected_stroke3 = "{\"l\":\"n\",\"c\":\"#00000040\",\"s\":50,\"p\":[-0.2,0.2,0.9,0.9]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with semitransparent line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    val layer2 = new Layer
    layerList.addLayer(layer2)
    layer2.setVisibility(false)
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":[],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with two layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(stroke2)
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":["+expected_stroke2+"],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)
    expected = expected_start(time)+"{\"s\":["+stroke.toShortJsonString.get+","+stroke2.toShortJsonString.get+","+stroke3.toShortJsonString.get+"],\"v\":true},{\"s\":["+stroke2.toShortJsonString.get+"],\"v\":false}]}"
    assert (testing.equals(expected), "doodle with lines on invisible layers not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(bezLine)
    val expected_bezier = "{\"l\":\"b\",\"c\":\"#ffffff44\",\"s\":10,\"p\":[-0.24,-0.24,1.112058,1.112058,2,22.53525,2.854,389.5]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":["+expected_stroke2+","+expected_bezier+"],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with bezier line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

    layer2.add(multi)
    val exp_dot = "{\"l\":\"n\",\"c\":\"#00f\",\"s\":1,\"p\":[55,55]}"
    val exp_dot2 = "{\"l\":\"n\",\"c\":\"#f00\",\"s\":2,\"p\":[300,300]}"
    val expected_multi = "{\"l\":\"m\",\"ss\":["+exp_dot+","+exp_dot2+"]}"
    expected = expected_start(time)+"{\"s\":["+expected_stroke+","+expected_stroke2+","+expected_stroke3+"],\"v\":true},{\"s\":["+expected_stroke2+","+expected_bezier+","+expected_multi+"],\"v\":false}]}"
    testing = layerList.toShortJsonString(time, chain)
    assert (testing.equals(expected), "doodle with multi line not correctly formatted when saving it.\nFound:   "+testing+"\nExpected:"+expected)

  }

  "LayerList.toShortJsonString" should "return correct paint time and DoodleModel.load should load it" in {
    val doodleModel = new DoodleModel
    val layerList = doodleModel.layers
    var chain = "abcdefg"
    var testing = layerList.toShortJsonString(1,chain)
    var expected = "{\"v\":499,\"d\":\""+chain+"\",\"t\":1,\"l\":[{\"s\":[],\"v\":true}]}"
    assert (testing.equals(expected), "doodle with painttime 1 wrong: \nexpected "+expected+"\ngot      "+testing)
    doodleModel.load(JsonParse.parseSave(testing))
    assert(doodleModel.getPaintTime.equals(1), "save with painttime 1 loaded wrong: "+doodleModel.getPaintTime)
    testing = layerList.toShortJsonString(123586,chain)
    expected = "{\"v\":499,\"d\":\""+chain+"\",\"t\":123586,\"l\":[{\"s\":[],\"v\":true}]}"
    assert (testing.equals(expected), "doodle with painttime 123586 wrong: \nexpected "+expected+"\ngot      "+testing)
    doodleModel.load(JsonParse.parseSave(testing))
    assert(doodleModel.getPaintTime.equals(1+123586), "save with painttime 123586 loaded wrong: "+doodleModel.getPaintTime)
  }

}

