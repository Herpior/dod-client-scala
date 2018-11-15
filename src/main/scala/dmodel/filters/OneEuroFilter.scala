package dmodel.filters

import dmodel.Coord

// based on http://cristal.univ-lille.fr/~casiez/1euro/
class OneEuroFilter() {
  var beta = 0.0001 //increase to reduce lag
  var mincutoff = 4.0 //decrease to reduce jitter but increase lag
  var dx = Coord(0)
  var dxdouble = 0.0
  val xfilt = new LowPassFilter
  val dxfilt = new LowPassFilter
  val dcutoff = 1.0 // what's this? derivative cutoff? this wasn't mentioned in the paper? apparently others use 1.0 as this value?
  private var prevTime = 0.0

  private var initialized = false
  private var initializeddouble = false

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
  def update(x:Coord, timestampInSeconds:Double) = {
    val freq = 1/(timestampInSeconds - prevTime)
    if (!initialized) {
      initialized = true
      dx = Coord(0)
    }
    else {
      if(initializeddouble){
        reset
        print("1€ Filter is already initialized as double, resetting.")
      }
      dx = x - xfilt.hatxprev * freq
    }
    val edx = dxfilt.filter(dx, alpha(freq, dcutoff))
    val cutoff = mincutoff + beta * edx.length
    prevTime = timestampInSeconds
    xfilt.filter(x, alpha(freq, cutoff))
  }

  def alpha(freq:Double, cutoff:Double) = {
    val tau = 1.0/(2*math.Pi*cutoff)
    val te = 1.0/freq
    1.0/(1+tau/te)
  }

  def update(x:Double, timestampInSeconds:Double) = {
    val freq = 1/(timestampInSeconds - prevTime)
    if (!initializeddouble) {
      initializeddouble = true
      dxdouble = 0.0
    }
    else {
      if(initialized){
        reset
        print("1€ Filter is already initialized as Coord, resetting.")
      }
      dxdouble = x - xfilt.hatxprevdouble * freq
    }
    val edx = dxfilt.filter(dx, alpha(freq, dcutoff))
    val cutoff = mincutoff + beta * edx.length
    prevTime = timestampInSeconds
    xfilt.filter(x, alpha(freq, cutoff))
  }

  def reset ={
    this.initialized = false
    this.initializeddouble = false
    this.xfilt.reset
    this.dxfilt.reset
  }


}

