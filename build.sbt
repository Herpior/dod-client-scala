name := "dod"

version := "0.1"

scalaVersion := "2.11.12"

// add dependencies on standard Scala modules, in a way
// supporting cross-version publishing
// taken from: http://github.com/scala/scala-module-dependency-sample
libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if Scala 2.12+ is used, use scala-swing 2.x
    case Some((2, scalaMajor)) if scalaMajor >= 12 =>
      libraryDependencies.value ++ Seq(
        //"org.scala-lang.modules" %% "scala-xml" % "1.1.1",
        //"org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1",
        "org.scala-lang.modules" %% "scala-swing" % "2.0.3")
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        //"org.scala-lang.modules" %% "scala-xml" % "1.1.1",
        //"org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1",
        "org.scala-lang.modules" %% "scala-swing" % "1.0.2")
    case _ =>
      // or just libraryDependencies.value if you don't depend on scala-swing
      libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
  }
}

// Gson (JsonParse)
libraryDependencies += "com.google.code.gson" % "gson" % "2.2"

// Apache http
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.6"

// Scalatest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"