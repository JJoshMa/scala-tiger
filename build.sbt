
import scala.scalanative.build._

lazy val commonSettings = Seq(
  scalaVersion := "3.3.3",
)

lazy val nasm = (project in file("."))
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings)
  .settings(
    logLevel := Level.Info,
    nativeConfig ~= { c =>
      c.withLTO(LTO.none)
        .withMode(Mode.debug)
        .withGC(GC.immix)
    },
  )
  .dependsOn(backend)

lazy val backend = (project in file("backend"))
  .settings(commonSettings)
