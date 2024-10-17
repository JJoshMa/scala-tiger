
lazy val BINARY_NAME = "tiger"
lazy val common = Seq(
    scalaVersion := "3.3.3"
)

lazy val root = project
    .in(file("."))
    .aggregate(cli, backend)

lazy val cli = project
    .in(file("cli"))
    .enablePlugins(ScalaNativePlugin)
    .dependsOn(backend)
    .settings(common)

lazy val backend = project.in(file("backend"))
    .enablePlugins(ScalaNativePlugin)
    .settings(common)

lazy val buildDebugBinary = taskKey[File]("")
buildDebugBinary := {
    writeBinary(
        source = (cli / Compile / nativeLink).value,
        destinationDir = (ThisBuild / baseDirectory).value / "out" / "debug",
        log = sLog.value,
        platform = None
    )
}

lazy val buildReleaseBinary = taskKey[File]("")
buildReleaseBinary := {
    writeBinary(
        source = (cli / Compile / nativeLinkReleaseFast).value,
        destinationDir = (ThisBuild / baseDirectory).value / "out" / "release",
        log = sLog.value,
        platform = None
    )
}

lazy val buildPlatformBinary = taskKey[File]("")
buildPlatformBinary := {
    writeBinary(
        source = (cli / Compile / nativeLinkReleaseFast).value,
        destinationDir = (ThisBuild / baseDirectory).value / "out" / "release",
        log = sLog.value,
        platform = Some(Platform.target)
    )
}

def writeBinary(source: File,
                destinationDir: File,
                log: sbt.Logger,
                platform: Option[Platform.Target]): File = {
    cli.enablePlugins(ScalaNativePlugin)
    backend.enablePlugins(ScalaNativePlugin)
    
    val name = platform match {
        case None => BINARY_NAME
        case Some(target) =>
            val ext = target.os match {
                case Platform.OS.Windows => ".exe"
                case _ => ""
            }
            
            BINARY_NAME + "-" + ArtifactNames.coursierString(target) + ext
    }
    
    val dest = destinationDir / name
    
    IO.copyFile(source, dest)
    
    log.info(s"Binary [$name] built in ${dest}")
    
    dest
}
