import akka.actor.{ActorSystem, Props}
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.RoundRobinPool

object TestSystem extends App {

  val system = ActorSystem("test-system")


  /*val roundRobinPool = RoundRobinPool(nrOfInstances = 4)
  val  clusterRouterSettings = ClusterRouterPoolSettings(totalInstances = 4,
    maxInstancesPerNode = 2, allowLocalRoutees = true, useRole = None)

  val clusterPool = ClusterRouterPool(roundRobinPool,clusterRouterSettings)

  val router = system.actorOf(clusterPool.props(Ponger.props(pinger)))*/


  //val system.actorOf(Props[Logger])

}
