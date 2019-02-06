package dmodel.filters

import dmodel.Coord


class LowPassFilter {
  var hatxprev = Coord(0)
  var prev = Coord(0)
  private var initialized = false
  /*
    EXT: First time flag: firstTime set to true
    IN : Noisy sample value : x
    Alpha value : alpha
    OUT: Filtered value */
  def filter(x:Coord, alpha:Double): Coord = {
    var hatx = x
    prev = x
    if (initialized) {
      hatx = x * alpha + hatxprev * (1 - alpha)
    }
    else {
      initialized = true
    }
    hatxprev = hatx
    hatx
  }
  def reset(): Unit = {
    initialized = false
  }
}
