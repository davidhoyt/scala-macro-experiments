import sbt._

object Settings {
  val project            = "DDL"

  val company            = "davidhoyt"

  val organization       = "org.github"

  val homepage           = "https://github.com/davidhoyt/scala-macro-experiments/"

  val vcsSpecification   = "git@github.com:davidhoyt/scala-macro-experiments/scala-macro-experiments.git"

  val licenses           = Seq(
    License(
      name  = "The Apache Software License, Version 2.0",
      url   = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    )
  )

  val developers         = Seq(
    Developer(
        id              = "David Hoyt"
      , name            = "David Hoyt"
      , email           = "dhoyt@hoytsoft.org"
      , url             = "http://www.hoytsoft.org/"
      , organization    = "HoytSoft"
      , organizationUri = "http://www.hoytsoft.org/"
      , roles           = Seq("architect", "developer")
    )
  )

  val scalaVersion       = "2.10.2"

  val scalacOptions      = Seq("-deprecation", "-unchecked", "-feature", "-Xelide-below", "900")
  val javacOptions       = Seq("-Xlint:unchecked")

  val prompt             = GitPrompt.build
}

