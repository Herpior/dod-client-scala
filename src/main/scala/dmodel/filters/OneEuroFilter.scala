package dmodel.filters

import dmodel.Coord

// based on http://cristal.univ-lille.fr/~casiez/1euro/
class OneEuroFilter() {
  var beta = 1e-12 //increase to reduce lag
  var mincutoff = 4.0 //decrease to reduce jitter but increase lag
  var dcutoff = 1.0 // this also has some effect

  var dx = Coord(0)
  val xfilt = new LowPassFilter
  val dxfilt = new LowPassFilter
  private var prevTime = 0.0

  private var initialized = false

  /*
  EXT:
  First time flag: firstTime set to true
  Data update rate: freq
  Minimum cutoff frequency: mincutoff
  Cutoff slope: beta
  Low-pass filter: xfilt
  Cutoff frequency for derivate: dcutoff
  Low-pass filter for derivate: dxfilt

  IN :
  Noisy sample value: x
  Timestamp in seconds: timestampInSeconds

  OUT: Filtered sample value */
  def update(x:Coord, timestampInSeconds:Double):Coord = {
    val freq = 1/(timestampInSeconds - prevTime)
    if (!initialized) {
      initialized = true
      dx = Coord(0)
    }
    else {
      dx = x - xfilt.prev * freq
    }
    val edx = dxfilt.filter(dx, alpha(freq, dcutoff))
    val cutoff = mincutoff + beta * edx.length
    prevTime = timestampInSeconds
    val res = xfilt.filter(x, alpha(freq, cutoff))
    res
  }

  def alpha(freq:Double, cutoff:Double): Double = {
    val tau = 1.0/(2*math.Pi*cutoff)
    val te = 1.0/freq
    1.0/(1+tau/te)
  }

  // just make sure you use same type every time..
  def update(x:Double, timestampInSeconds:Double):Double = {
    update(Coord(x, 0), timestampInSeconds).x
  }

  def reset(): Unit ={
    this.initialized = false
    this.xfilt.reset
    this.dxfilt.reset
  }


}

