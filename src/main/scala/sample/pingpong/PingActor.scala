package sample.pingpong

import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.cluster.Cluster
import akka.routing.FromConfig
import akka.actor.ReceiveTimeout
import PongActor.Pong
import scala.util.Try
import scala.concurrent.Await

class PingActor() extends Actor with ActorLogging {

  import PingActor._
  val pong = context.actorOf(FromConfig.props(),
    name = "pongActorRouter")

  override def preStart(): Unit = {
    pong ! Ping
    context.setReceiveTimeout(10.seconds)
  }

  def receive = {
    case Pong => println(s"${self.path} received Ping")
//      pong ! Ping
    case ReceiveTimeout => log.info("Timeout")
  }
}

object PingActor {
  case object Ping

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.parseString("akka.cluster.roles = [frontend]").
      withFallback(ConfigFactory.load("pingpong"))

    val system = ActorSystem("ClusterSystem", config)

    Cluster(system) registerOnMemberUp {
      system.actorOf(Props[PingActor], name = "pingActor")
    }

    Cluster(system).registerOnMemberRemoved {
      system.registerOnTermination(System.exit(0))
      system.terminate()

      // In case ActorSystem shutdown takes longer than 10 seconds,
      // exit the JVM forcefully anyway.
      // We must spawn a separate thread to not block current thread,
      // since that would have blocked the shutdown of the ActorSystem.
      new Thread {
        override def run(): Unit = {
          if (Try(Await.ready(system.whenTerminated, 10.seconds)).isFailure)
            System.exit(-1)
        }
      }.start()
    }
  }
}
