
import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.routing.FromConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.StdIn

object PingPong extends App {

  import Ponger._

  implicit val system1 = ActorSystem("ClusterSystem")
  implicit val system2 = ActorSystem("ClusterSystem")
  implicit val system3 = ActorSystem("ClusterSystem")

  while (Cluster(system1).state.members.size != 3) {
    Thread.sleep(250)
  }

  val pinger = system1.actorOf(Pinger.props, "pinger")
  val pool = system1.actorOf(FromConfig.props(Ponger.props(pinger)), "echo")
  system1.scheduler.schedule(1.second, 1.second, pool, Ping)

  println("Enter to quit")
  StdIn.readLine()
  Await.result(Future.traverse(List(system1, system2, system3))(system => system.terminate()), Duration.Inf)

}






