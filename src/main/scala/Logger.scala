import akka.actor.{Actor, ActorLogging}

class Logger extends Actor with ActorLogging {
  log.info("logger started!!")
  def receive = {
    case msg => log.info(s"Got msg: $msg")
  }
}
