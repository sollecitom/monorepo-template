package plugins.publish

import RepositoryConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get


class MavenPublicationPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.pluginManager.apply(MavenPublishPlugin::class)

        project.extensions.configure(PublishingExtension::class) {
            repositories {
                RepositoryConfiguration.Publications.apply(this, project)
            }
            publications {
                create("${project.name}-maven", MavenPublication::class.java) {
                    groupId = project.rootProject.group.toString()
                    artifactId = project.name
                    version = project.rootProject.version.toString()
                    from(project.components["java"])
                    println("Created publication ${groupId}:${artifactId}:${version}")
                }
            }
        }
    }
}