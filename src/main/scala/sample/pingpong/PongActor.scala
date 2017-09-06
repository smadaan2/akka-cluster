package sample.pingpong

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import sample.pingpong.PingActor.Ping

class PongActor extends Actor with ActorLogging {

  import PongActor._

  def receive = {
    case Ping => println(s"${self.path} received Ping")
      sender() ! Pong
  }
}

object PongActor {

  case object Pong

  def main(args: Array[String]): Unit = {
    // Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]")).
      withFallback(ConfigFactory.load("pingpong"))

    val system = ActorSystem("ClusterSystem", config)
    system.actorOf(Props[PongActor], name = "pongActor")
  }
}