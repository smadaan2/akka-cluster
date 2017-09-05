package sample.pingpong


object PingPongApp {
  def main(args: Array[String]): Unit = {
    // starting 3 backend nodes and 1 frontend node
    PongActor.main(Seq("2551").toArray)
    PongActor.main(Seq("2552").toArray)
    PongActor.main(Array.empty)
    PingActor.main(Array.empty)

  }
}