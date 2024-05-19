@file:Suppress("UnstableApiUsage")

rootProject.name = "monorepo-template"

library("libs", "chassis", "kotlin", "extensions")
library("libs", "chassis", "core", "domain")
library("libs", "chassis", "core", "test", "utils")

service("services", "service-1", "domain")

fun library(vararg pathSegments: String) = module(exclude = "libs", pathSegments = pathSegments)

fun service(vararg pathSegments: String) = module(exclude = "services", pathSegments = pathSegments)

fun module(exclude: String, vararg pathSegments: String) {

    val projectName = pathSegments.last()
    val path = pathSegments.dropLast(1)
    val group = path.minus(exclude).joinToString(separator = "-")
    val directory = path.joinToString(separator = "/", prefix = "./")
    val fullProjectName = "${if (group.isEmpty()) "" else "$group-"}$projectName"

    include(fullProjectName)
    project(":$fullProjectName").projectDir = mkdir("$directory/$projectName")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")