import Pinger.Pong
import Ponger.Ping
import akka.actor.{Actor, PoisonPill, Props}

import scala.language.postfixOps

class Pinger extends Actor {
  var countDown = 10

  def receive = {
    case Pong => "pong"
  }
}

object Pinger {
  case object Pong
  def props = Props(new Pinger)
}
