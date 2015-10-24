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
  "org.slf4j" % "slf4j-nop" % "1.6.0-RC0",

  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
