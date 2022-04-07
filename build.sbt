name := "Peaceland project"

scalaVersion := "2.11.12"

version := "0.1"

libraryDependencies += "org.apache.kafka"  % "kafka-clients" % "2.4.1"
libraryDependencies += "org.apache.kafka" %% "kafka"         % "2.4.1"

libraryDependencies += "it.bitbl" %% "scala-faker" % "0.4"

libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.8"
libraryDependencies += "org.apache.spark" %% "spark-core"           % "2.4.8"
libraryDependencies += "org.apache.spark" %% "spark-sql"            % "2.4.8"
libraryDependencies += "org.apache.spark" %% "spark-hive"           % "2.4.8"

libraryDependencies += "net.liftweb"   %% "lift-json"  % "3.1.1"
// https://mvnrepository.com/artifact/org.postgresql/postgresql
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.1"

