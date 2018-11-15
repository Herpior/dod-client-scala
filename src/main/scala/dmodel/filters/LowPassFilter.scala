package dmodel.filters

import dmodel.Coord


class LowPassFilter {
  var hatxprev = Coord(0)
  var hatxprevdouble = 0.0
  private var initialized = false
  private var initializeddouble = false
  /*
    EXT: First time flag: firstTime set to true
    IN : Noisy sample value : x
    Alpha value : alpha
    OUT: Filtered value */
  def filter(x:Coord, alpha:Double) = {
    var hatx = x
    if (initialized) {
      hatx = x * alpha + hatxprev * (1 - alpha)
    }
    else {
      if(initializeddouble){
        reset
        print("Filter is already initialized as double, resetting.")
      }
      initialized = true
    }
    hatxprev = hatx
    hatx
  }
  def filter(x:Double, alpha:Double) = {
    var hatx = x
    if (initializeddouble) {
      hatx = x * alpha + hatxprevdouble * (1 - alpha)
    }
    else {
      if(initialized){
        reset
        print("Filter is already initialized as Coord, resetting.")
      }
      initializeddouble = true
    }
    hatxprevdouble = hatx
    hatx
  }
  def reset: Unit = {
    initialized = false
    initializeddouble = false
  }
}
