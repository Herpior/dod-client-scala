package dmodel

/**
  * Json class for gson to load state data from the servers.
  * That is, whether the chain has expired, is drawing prompt or is a doodle to be described,
  * along with some related data.

  * @author Qazhax
  */

import dmodel.dpart.JsonDoodle
import view.{DoodlingPanel, PhrasingPanel, PlayPanel, StalingPanel}

import scala.swing.Panel

class JsonState {
  var activeState:String = _
  var private_id:String = _
  var group_id:String = _
  var staleChain_id:String = _
  var skipsInfo:JsonSkips = _
  var isFinishingChain:Boolean = _
  var isStartingChain:Boolean = _
  var phrase:String = _
  var googleSafe:String = _
  var isPartial:Boolean = _
  var layout:Boolean = _
  var html:String = _
  var doodle:JsonDoodle = _
  
  /*def states = {
    "draw"
    "stale"
    "phrase"
  }*/
  def finishing: Boolean = {
    /*isFinishingChain != null &&*/ isFinishingChain
  }
  def starting: Boolean = {
    /*isStartingChain != null &&*/ isStartingChain
  }
  def randomizer: Boolean = {
    starting || group_id == "gJzy1S_3_" || group_id == "EGb3cV8Bo8"
  }
  def getPhrase: String = {
    if(phrase!=null)phrase else ""
  }
  def getDoodle: JsonDoodle = {
    if(doodle!=null)doodle else new JsonDoodle
  }
}
/*
class JsonSkipInfo{
  var skipsAvailable:Int = _
  var skipDurationHours:Int = _
  var skipsPerDuration:Int = _
}*/