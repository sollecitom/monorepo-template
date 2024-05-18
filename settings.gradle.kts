@file:Suppress("UnstableApiUsage")

rootProject.name = "monorepo-template"

module("libs", "chassis", "kotlin", "extensions")
module("libs", "chassis", "core", "domain")
module("libs", "chassis", "core", "test", "utils")

module("services", "service-1", "domain")

fun module(vararg pathSegments: String) {
    val projectName = pathSegments.last()
    val path = pathSegments.dropLast(1)
    val group = path.joinToString(separator = "-")
    val directory = path.joinToString(separator = "/", prefix = "./")

    include("${rootProject.name}-${if (group.isEmpty()) "" else "$group-"}$projectName")
    project(":${rootProject.name}-${if (group.isEmpty()) "" else "$group-"}$projectName").projectDir = mkdir("$directory/$projectName")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")