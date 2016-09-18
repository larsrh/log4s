organization := "info.hupel.fork.org.log4s"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0-RC1")

name := "log4s"

libraryDependencies ++= Seq(
  "org.slf4j"      %  "slf4j-api"       % "1.7.21",
  "ch.qos.logback" %  "logback-classic" % "1.1.7"            % "test",
  "org.scalatest"  %% "scalatest"       % "3.0.0"            % "test",
  "org.scala-lang" %  "scala-reflect"   % scalaVersion.value % "provided"
)

unmanagedSourceDirectories in Compile <+= (scalaBinaryVersion, baseDirectory) { (ver, dir) =>
  ver match {
    case "2.10" => dir / "src" / "main" / "scala-2.10"
    case _      => dir / "src" / "main" / "scala-2.11"
  }
}

homepage := Some(url("https://github.com/larsrh/log4s"))

licenses := Seq(
  "Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <developers>
    <developer>
      <id>sarah</id>
      <name>Sarah Gerweck</name>
      <email>sarah.a180@gmail.com</email>
      <url>https://github.com/sarahgerweck</url>
      <timezone>America/Los_Angeles</timezone>
    </developer>
    <developer>
      <id>larsrh</id>
      <name>Lars Hupel</name>
      <url>http://lars.hupel.info</url>
    </developer>
  </developers>
  <scm>
    <connection>scm:git:github.com/larsrh/log4s.git</connection>
    <developerConnection>scm:git:git@github.com:larsrh/log4s.git</developerConnection>
    <url>https://github.com/larsrh/log4s</url>
  </scm>
)

credentials += Credentials(
  Option(System.getProperty("build.publish.credentials")) map (new File(_)) getOrElse (Path.userHome / ".ivy2" / ".credentials")
)


// Release stuff

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true)
)
