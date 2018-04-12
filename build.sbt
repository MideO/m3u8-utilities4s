lazy val commonSettings = Seq(
  name := "m3u8-utilities4s",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.12.5",
  crossScalaVersions := Seq("2.11.12", "2.12.5"),
  scalacOptions ++= Seq(
    "-feature",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
    "-language:postfixOps"
  ),
  description := "m3u8 utilities for scala",
  resolvers ++= Seq(
    "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
    "Sonatypes" at "https://oss.sonatype.org/content/repositories/releases",
    Resolver.DefaultMavenRepository
  ),
  organization := "com.github.mideo",
  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
  homepage := Some(url("https://github.com/MideO/m3u8-utilities4s")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/MideO/m3u8-utilities4s"),
      "scm:git@github.com/MideO/m3u8-utilities4s"
    )
  ),
  developers := List(
    Developer(
      id = "mideo",
      name = "Mide Ojikutu",
      email = "mide.ojikutu@gmail.com",
      url = url("https://github.com/MideO")
    )
  ),
) ++ publishSettings ++ releaseSettings

lazy val jvmTestSettings = Seq(
  resourceDirectory in Test := baseDirectory.value / "src/test/resources",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.4" % Test,
    "org.mockito" % "mockito-all" % "1.10.19" % Test,
    "com.github.tomakehurst" % "wiremock" % "2.14.0" % Test
  ),
  parallelExecution in Test := false
)


lazy val publishSettings = {
  val oss_user = if (sys.env.keySet.contains("OSS_USERNAME")) sys.env("OSS_USERNAME") else ""
  val oss_pass = if (sys.env.keySet.contains("OSS_PASSWORD")) sys.env("OSS_PASSWORD") else ""
  val gpg_pass = if (sys.env.keySet.contains("GPG_PASSWORD")) sys.env("GPG_PASSWORD").toCharArray else Array.emptyCharArray

  Seq(
    pomIncludeRepository := { _ => true },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    credentials += Credentials(
      "Sonatype Nexus Repository Manager",
      "oss.sonatype.org", oss_user, oss_pass),
    pgpPassphrase := Some(gpg_pass),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },

  )
}

lazy val releaseSettings = {
  import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

  Seq(
    releaseVersionBump := sbtrelease.Version.Bump.Next,
    releaseIgnoreUntrackedFiles := true,
    releaseCrossBuild := true,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      setNextVersion,
      commitNextVersion,
      pushChanges
    ),
  )
}

lazy val noPublish = Seq(
  publish := {},
  publishLocal := {},
  PgpKeys.publishSigned := {},
  publishArtifact := false
)

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(noPublish)
  .aggregate(m3u8Utilities4sJVM, m3u8Utilities4sJS)

lazy val `m3u8-utilities4s` = crossProject
  .in(file("."))
  .settings(commonSettings: _*)
  .jvmSettings(jvmTestSettings ++ Seq(libraryDependencies += "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % Provided))

lazy val m3u8Utilities4sJVM = `m3u8-utilities4s`.jvm
lazy val m3u8Utilities4sJS = `m3u8-utilities4s`.js
