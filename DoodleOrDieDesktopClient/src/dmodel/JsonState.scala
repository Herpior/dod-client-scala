package dmodel

import view.DoodlingPanel
import view.PhrasingPanel
import view.StalingPanel

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
  def finishing = {
    /*isFinishingChain != null &&*/ isFinishingChain
  }
  def starting = {
    /*isStartingChain != null &&*/ isStartingChain
  }
  def randomizer = {
    starting || group_id == "gJzy1S_3_" || group_id == "EGb3cV8Bo8"
  }
  def getPhrase = {
    if(phrase!=null)phrase else ""
  }
  def getDoodle = {
    if(doodle!=null)doodle else new JsonDoodle
  }
  def toPlayPanel={
    //println(activeState+" - "+private_id+"/"+staleChain_id+" - "+group_id)
    activeState match{
      case "draw"=>
        require(group_id!=null &&group_id.length>0)
        require(private_id!=null &&private_id.length>0)
        if(starting)phrase = "Start a new chain! Draw anything you want!"
        new DoodlingPanel(group_id,private_id,getPhrase,finishing,randomizer)
      case "phrase"=>
        require(group_id!=null &&group_id.length>0)
        require(private_id!=null &&private_id.length>0)
        new PhrasingPanel(group_id,private_id,getDoodle,starting,finishing,randomizer)
      case _ =>
        new StalingPanel
    }
  }
}
/*
class JsonSkipInfo{
  var skipsAvailable:Int = _
  var skipDurationHours:Int = _
  var skipsPerDuration:Int = _
}*/