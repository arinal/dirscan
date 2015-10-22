name := """dirscan"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "2.2.9",
  "com.h2database"  %  "h2"                % "1.4.190",
  "ch.qos.logback"  %  "logback-classic"   % "1.1.3",

  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
