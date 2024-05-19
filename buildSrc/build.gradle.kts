plugins {
    alias(libs.plugins.kotlin.jvm)
    `kotlin-dsl`
    alias(libs.plugins.com.github.ben.manes.versions)
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.semver4j)
    implementation(libs.kotlin.gradle.plugin)
}

// TODO read and use https://docs.gradle.org/current/userguide/custom_plugins.html#sec:build_script_plugins for different types of plugins
// TODO read and use https://docs.gradle.org/current/userguide/implementing_gradle_plugins_binary.html
// TODO read and use https://docs.gradle.org/current/userguide/custom_gradle_types.html#service_injection