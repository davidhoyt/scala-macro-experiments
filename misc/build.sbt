
conflictWarning ~= { _.copy(failOnConflict = false) }

resolvers += Resolver.sonatypeRepo("snapshots")

//addCompilerPlugin("org.scala-lang.plugins" % "macro-paradise" % "2.0.0-SNAPSHOT" cross CrossVersion.full)

libraryDependencies += "com.chuusai" % "shapeless_2.11.0-M7" % "2.0.0-M1"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.2" % "test"

libraryDependencies += "junit" % "junit" % "4.11" % "test"

