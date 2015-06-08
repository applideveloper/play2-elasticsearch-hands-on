name := "play2-hands-on"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  //  jdbc,
  //  anorm,
  //  cache,
  //  ws,
  // Elasticsearch and JSON
  "com.typesafe.play"           %%  "play-slick"            % "0.8.1",
  "com.sksamuel.elastic4s"      %%  "elastic4s"             % "1.5.6"
    excludeAll(
    ExclusionRule(organization = "org.scala-lang", name = "scala-library"),
    //      ExclusionRule(organization = "org.apache.lucene", name = "lucene-analyzers-common"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-highlighter"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-grouping"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-join"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-memory"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-misc"),
    //      ExclusionRule(organization = "org.apache.lucene", name = "lucene-queries"),
    //      ExclusionRule(organization = "org.apache.lucene", name = "lucene-queryparser"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-sandbox"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-spatial"),
    ExclusionRule(organization = "org.apache.lucene", name = "lucene-suggest")
    )
)
