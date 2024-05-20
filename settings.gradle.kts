@file:Suppress("UnstableApiUsage")

rootProject.name = "monorepo-template"

library("chassis", "kotlin", "extensions")
library("chassis", "core", "domain")
library("chassis", "core", "test", "utils")

service("service-1", "domain")

fun library(vararg pathSegments: String) = module(rootFolder = "libs", pathSegments = pathSegments)

fun service(vararg pathSegments: String) = module(rootFolder = "services", pathSegments = pathSegments)

fun tool(vararg pathSegments: String) = module(rootFolder = "tools", pathSegments = pathSegments)

fun module(rootFolder: String, vararg pathSegments: String) {

    val projectName = pathSegments.last()
    val path = listOf(rootFolder) + pathSegments.dropLast(1)
    val group = path.minus(rootFolder).joinToString(separator = "-")
    val directory = path.joinToString(separator = "/", prefix = "./")
    val fullProjectName = "${if (group.isEmpty()) "" else "$group-"}$projectName"

    include(fullProjectName)
    project(":$fullProjectName").projectDir = mkdir("$directory/$projectName")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")