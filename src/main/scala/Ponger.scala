import Pinger.Pong
import Ponger.Ping
import akka.actor.{Actor, ActorRef, Props}

import scala.language.postfixOps

class Ponger(pinger: ActorRef) extends Actor {
  def receive = {
    case Ping =>
      println(s"${self.path} received ping")
      pinger ! Pong
  }
}

object Ponger {
  case object Ping
  def props(pinger: ActorRef) = Props(new Ponger(pinger))
}




