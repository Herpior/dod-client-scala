package dmodel.filters

abstract class SmoothingFilter[T] {
  def filter(x:T, timestamp:Double):T
  def reset()
}