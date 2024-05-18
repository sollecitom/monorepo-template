package plugins.publish

import RepositoryConfiguration
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get


class MavenPublicationPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.pluginManager.apply(MavenPublishPlugin::class.java)

        project.extensions.configure(PublishingExtension::class, object : Action<PublishingExtension> {

            override fun execute(publishing: PublishingExtension) {

                publishing.repositories(object : Action<RepositoryHandler> {
                    override fun execute(repositories: RepositoryHandler) {
                        RepositoryConfiguration.Publications.apply(repositories, project)
                    }
                })

                publishing.publications(object : Action<PublicationContainer> {

                    override fun execute(publications: PublicationContainer) {

                        publications.create("${project.name}-maven", MavenPublication::class.java, object : Action<MavenPublication> {

                            override fun execute(publication: MavenPublication) {
                                publication.groupId = project.rootProject.group.toString()
                                publication.artifactId = project.name
                                publication.version = project.rootProject.version.toString()
                                publication.from(project.components["java"])
                                println("Created publication ${publication.groupId}:${publication.artifactId}:${publication.version}")
                            }
                        })
                    }
                })
            }
        })
    }
}