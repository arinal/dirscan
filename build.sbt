name := """dirscan"""

version := "1.0"

scalaVersion := "2.11.7"

import sbtassembly.AssemblyPlugin.defaultShellScript

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript))

assemblyJarName in assembly := "dirscan"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "2.2.9",
  // "org.xerial"      % "sqlite-jdbc"        % "3.7.2",
  "com.h2database"  %  "h2"                % "1.4.190",
  "ch.qos.logback"  %  "logback-classic"   % "1.1.3",

  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
