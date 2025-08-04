ThisBuild / scalaVersion := "3.7.1"
ThisBuild / organization := "io.github.wiresynth.sc"

ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / homepage := Some(url("https://github.com/wiresynth"))
ThisBuild / licenses := List(
  "LGPL-2.1" -> url(
    "https://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html"
  )
)
ThisBuild / developers := List(
  Developer(
    "jellyterra",
    "Jelly Terra",
    "jellyterra@jellyterra.com",
    url("https://jellyterra.com")
  )
)

val plugin = (project in file("plugin"))
  .settings(
    name := "plugin",
    libraryDependencies += "org.scala-lang" % "scala3-compiler_3" % scalaVersion.value % "provided"
  )

val core = (project in file("core"))
  .settings(
    Compile / scalaSource := baseDirectory.value / "src",
    name := "core",
    libraryDependencies += "io.github.p-org.solvers" % "z3" % "4.8.14-v5",
    scalacOptions += s"-Xplugin:${(plugin / Compile / packageBin).value.getAbsolutePath}"
  )
  .dependsOn(plugin)

val generator = (project in file("generator"))
  .settings(
    Compile / scalaSource := baseDirectory.value / "src",
    name := "generator"
  )
  .dependsOn(core)

val root = (project in file("."))
  .settings(
    publish / skip := true,
    name := "wiresynth"
  )
  .aggregate(plugin, core, generator)
