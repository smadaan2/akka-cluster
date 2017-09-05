name := "Akka-Cluster"

version := "0.1"

scalaVersion := "2.12.3"

val akkaVersion = "2.5.4"

libraryDependencies ++= Seq(
"com.typesafe.akka" %% "akka-actor" % akkaVersion,
"com.typesafe.akka" %% "akka-remote" % akkaVersion,
"com.typesafe.akka" %% "akka-cluster" % akkaVersion,
"com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
"com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
"com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
"org.scalatest" %% "scalatest" % "3.0.1" % Test
)
