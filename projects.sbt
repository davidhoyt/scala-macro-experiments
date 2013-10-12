import ProjectTemplates._


lazy val aaa_default_project = root

lazy val root = Root(
    dsl_core
  , dsl_macros
  , dsl
)

lazy val dsl_core     = Module("dsl-core")

lazy val dsl_macros   = Module("dsl-macros")
  .dependsOn(dsl_core   % "compile")

lazy val dsl          = Module("dsl")
  .dependsOn(dsl_core   % "compile")
  .dependsOn(dsl_macros % "compile")

