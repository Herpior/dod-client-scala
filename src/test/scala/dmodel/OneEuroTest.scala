package dmodel

import java.io.File

import dmodel.filters.{LowPassFilter, OneEuroFilter}

import scala.collection.mutable
//import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable
import scala.io.Source
import scala.util.Random

//@RunWith(classOf[JUnitRunner])
class OneEuroTest extends FlatSpec with Matchers {

  "OneEuroFilter.filter" should "return same Coord on first update" in
    {
      val filter = new OneEuroFilter()
      val coord = new Coord(0.234, 345.6243)
      val res = filter.update(coord, 120)
      assert (res.dist(coord)<0.000001,res + " not equal to "+ coord)
      filter.reset
      val coord2 = new Coord(70.35, -5.12438)
      val res2 = filter.update(coord2, 20)
      assert (res2.dist(coord2)<0.000001,res2 + " not equal to "+ coord2)
    }

  "OneEuroFilter.filter" should "return same values as an existing solution, with the same input" in
    {
      val filter = new OneEuroFilter()
      filter.beta = 0.0
      filter.mincutoff = 1.0
      val rng = new Random(28975)
      val signal = mutable.Buffer[Coord]()
      val noisySignal = mutable.Buffer[Coord]()
      val filtered = mutable.Buffer[Coord]()
      val timestamps = mutable.Buffer[Double]()
      val start = 0
      val end = 1000

      var prev = Coord(50.0, 0)
      var prevTime = 1532.0
      var delta = Coord(0.0, 0)
      for(i <- start to end) {
        delta += Coord(((rng.nextDouble()*2-1)*5*1000).toInt*0.001, 0)
        val x = prev + delta
        val noisy = x + Coord(rng.nextDouble()*10, 0)
        //val rate = ((30 + rng.nextDouble()*30)*100).toInt*0.01
        val timestamp = prevTime + 0.16 + (rng.nextDouble()*0.32*100).toInt*0.01
        prevTime = timestamp
        //val freq = 1.0/(timestamp - prevTime)

        timestamps += timestamp
        signal += x
        noisySignal += noisy
        filtered += filter.update(noisy, timestamp)
      }
      val avg_rate = prevTime-1532.0 / timestamps.length
      val resfile = new File("one_euro_test2.txt")
      io.LocalStorage.printToFile(resfile){
        writer =>
          writer.write("#SRC dodclient one euro implementation in 1d\n")
          writer.write("#CFG {'freq':"+avg_rate+", 'mincutoff':"+filter.mincutoff+", 'beta':"+filter.beta+", 'dcutoff':"+filter.dcutoff+"}\n")
          writer.write("#LOG timestamp, signal, noisy, filtered\n")
          for(i<-start to end) {
            writer.write(
              timestamps(i)+","+
              signal(i).x+","+
              noisySignal(i).x+","+
              filtered(i).x+"\n")
          }
          writer.close()
      }
      assert (!filtered.exists(c=>c.y != 0),"found nonzero y values despite using only x: "+filtered.find(c=>c.y != 0).getOrElse("none found"))
      val lines1 = Source.fromFile("one_euro_test.txt").getLines() //pre-computed values with the same input
      val lines2 = Source.fromFile("one_euro_test2.txt").getLines()
      while(lines1.hasNext && lines2.hasNext) {
        assert(lines1.next().equals(lines2.next()),"the values are not the same as the expected values")
      }
    }

  "OneEuroFilter.filter" should "return values somewhat close to the original values, using noisy signal" in
    {
      val filter = new OneEuroFilter()
      val rng = new Random(35975)
//      val signal = mutable.Buffer[Coord]()
//      val noisySignal = mutable.Buffer[Coord]()
//      val filtered = mutable.Buffer[Coord]()
//      val timestamps = mutable.Buffer[Double]()
      val start = 0
      val end = 1000

      var prev = Coord(60.0, 0)
      var prevTime = 1572.0
      var delta = Coord(0.0, 0)
      for(i <- start to end) {
        delta += Coord(((rng.nextDouble()*2-1)*5*1000).toInt*0.001, 0)
        val x = prev + delta
        val noisy = x + Coord(rng.nextDouble()*10, 0)
        //val rate = ((30 + rng.nextDouble()*30)*100).toInt*0.01
        val timestamp = prevTime + 0.16 + (rng.nextDouble()*0.32*100).toInt*0.01
        prevTime = timestamp
        //val rate = 1.0/timestamp - prevTime

//        timestamps += timestamp
//        signal += x
//        noisySignal += noisy
        val filtered = filter.update(noisy, timestamp)
        assert (filtered.dist(x) < 20,"the filter returns significantly wrong results")
      }
    }


  "OneEuroFilter.filter" should "return same values as doubles and Coords in 1d" in
    {
      val doublefilter = new OneEuroFilter()
      doublefilter.beta = 0.0 //TODO: check with nonzero beta
      doublefilter.mincutoff = 1.0
      val coordfilter = new OneEuroFilter()
      coordfilter.beta = 0.0
      coordfilter.mincutoff = 1.0
      val rng = new Random(59571)
      val start = 0
      val end = 1000

      var prev = Coord(60.0, 0)
      var prevTime = 1172.0
      var delta = Coord(0.0, 0)
      for(i <- start to end) {
        delta += Coord(((rng.nextDouble()*2-1)*5*1000).toInt*0.001, 0)
        val x = prev + delta
        val noisy = x + Coord(rng.nextDouble()*10, 0)
        val timestamp = prevTime + 0.16 + (rng.nextDouble()*0.32*100).toInt*0.01
        prevTime = timestamp

        val doublefiltered = doublefilter.update(noisy.x, timestamp)
        val coordfiltered = coordfilter.update(noisy, timestamp)
        assert (math.abs(doublefiltered - coordfiltered.x) < 0.00001,"filter returns different values with same input using doubles and coords")
      }
    }

  "OneEuroFilter.filter" should "return same Coord displacements in all directions" in
    {
      val epsilon = 1e-9
      val filter = new OneEuroFilter()
      var coord = Coord(0)
      var res = filter.update(coord, 120)
      val results = mutable.Buffer[Coord](res)
      assert (res.dist(coord)<0.000001,res + " not equal to "+ coord)
      coord = Coord(1)
      res = filter.update(coord, 140)
      results += res
      coord = Coord(2)
      res = filter.update(coord, 160)
      results += res
      coord = Coord(3)
      res = filter.update(coord, 170)
      results += res
      coord = Coord(5)
      res = filter.update(coord, 180)
      results += res
      coord = Coord(100)
      res = filter.update(coord, 200)
      results += res
      coord = Coord(101)
      res = filter.update(coord, 240)
      results += res


      filter.reset
      var coord2 = Coord(0)
      var res2 = filter.update(coord2, 120)
      val results2 = mutable.Buffer[Coord](res2)
      assert (res2.dist(coord2)<0.000001,res2 + " not equal to "+ coord2)
      coord2 = Coord(-1)
      res2 = filter.update(coord2, 140)
      results2 += res2
      coord2 = Coord(-2)
      res2 = filter.update(coord2, 160)
      results2 += res2
      coord2 = Coord(-3)
      res2 = filter.update(coord2, 170)
      results2 += res2
      coord2 = Coord(-5)
      res2 = filter.update(coord2, 180)
      results2 += res2
      coord2 = Coord(-100)
      res2 = filter.update(coord2, 200)
      results2 += res2
      coord2 = Coord(-101)
      res2 = filter.update(coord2, 240)
      results2 += res2

      for (i <- results.indices){
        assert(results(i).dist(-results2(i))<epsilon, "difference in results between 1,1 and -1,-1 directions: "+results(i)+" - " +results2(i))
      }

    }


  "LowPassFilter.filter" should "return same Coord displacements in all directions" in
    {
      val epsilon = 1e-9
      val filter = new LowPassFilter()
      var coord = Coord(0)
      var res = filter.filter(coord, 0.5)
      val results = mutable.Buffer[Coord](res)
      assert (res.dist(coord)<0.000001,res + " not equal to "+ coord)
      coord = Coord(1)
      res = filter.filter(coord, 0.25)
      results += res
      coord = Coord(2)
      res = filter.filter(coord, 0.5)
      results += res
      coord = Coord(3)
      res = filter.filter(coord, 0.5)
      results += res
      coord = Coord(5)
      res = filter.filter(coord, 0.75)
      results += res
      coord = Coord(100)
      res = filter.filter(coord, 0.5)
      results += res
      coord = Coord(101)
      res = filter.filter(coord, 0.15)
      results += res


      filter.reset
      var coord2 = Coord(0)
      var res2 = filter.filter(coord2, 0.5)
      val results2 = mutable.Buffer[Coord](res2)
      assert (res2.dist(coord2)<0.000001,res2 + " not equal to "+ coord2)
      coord2 = Coord(-1)
      res2 = filter.filter(coord2, 0.25)
      results2 += res2
      coord2 = Coord(-2)
      res2 = filter.filter(coord2, 0.5)
      results2 += res2
      coord2 = Coord(-3)
      res2 = filter.filter(coord2, 0.5)
      results2 += res2
      coord2 = Coord(-5)
      res2 = filter.filter(coord2, 0.75)
      results2 += res2
      coord2 = Coord(-100)
      res2 = filter.filter(coord2, 0.5)
      results2 += res2
      coord2 = Coord(-101)
      res2 = filter.filter(coord2, 0.15)
      results2 += res2

      for (i <- results.indices){
        assert(results(i).dist(-results2(i))<epsilon, "difference in results between 1,1 and -1,-1 directions: "+results(i)+" - " +results2(i))
      }

    }


}

