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

class PingActor(upToN: Int,repeat: Boolean) extends Actor with ActorLogging {

  import PingActor._
  val pong = context.actorOf(FromConfig.props(Props[PongActor]),
    name = "pongActorRouter")
  implicit val executionContext = context.dispatcher

  override def preStart(): Unit = {
    sendJobs
    if(repeat) {
      context.setReceiveTimeout(10 seconds)
    }
  }

  def receive = {
    case pong @ Pong(n) =>
      println(s"${self.path} received $pong")
      if( n == upToN ) {
        if (repeat) sendJobs
        else context.stop(self)
      }
    case ReceiveTimeout =>
      log.info("Timeout")
      sendJobs
  }

  def sendJobs: Unit = {
    1 to upToN foreach (n => pong ! Ping(n))
    //context.system.scheduler.schedule(1.second,5.second)(pong ! Ping)
  }
}

object PingActor {
  case class Ping(number: Int)
  val upToN = 6
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.parseString("akka.cluster.roles = [frontend]").
      withFallback(ConfigFactory.load("pingpong"))

    val system = ActorSystem("ClusterSystem", config)

    Cluster(system) registerOnMemberUp {
      system.actorOf(Props(classOf[PingActor],upToN,true), name = "pingActor")
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
