package conventions.task.dependency.update

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent
import nl.littlerobots.vcu.plugin.VersionCatalogUpdateExtension
import nl.littlerobots.vcu.plugin.VersionCatalogUpdatePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

abstract class DependencyUpdateConvention : Plugin<Project> {

    override fun apply(project: Project) = with(project) { // TODO introduce an extension that allows to override these settings

        pluginManager.apply(VersionCatalogUpdatePlugin::class)
        pluginManager.apply(VersionsPlugin::class)

        afterEvaluate {

            tasks.withType<DependencyUpdatesTask> {
                checkConstraints = true
                checkBuildEnvironmentConstraints = false
                checkForGradleUpdate = true
                outputFormatter = "json,html"
                outputDir = "build/dependencyUpdates"
                reportfileName = "report"

                rejectVersionIf {
                    wouldDowngradeVersion() || wouldDestabilizeAStableVersion()
                }
            }

            extensions.configure<VersionCatalogUpdateExtension> {
                sortByKey.set(false)
                keep {
                    keepUnusedVersions.set(true)
                    keepUnusedLibraries.set(true)
                    keepUnusedPlugins.set(true)
                }
            }
        }
    }

    private val ComponentSelectionWithCurrent.currentSemanticVersion: DependencyVersion get() = DependencyVersion(currentVersion)
    private val ComponentSelectionWithCurrent.candidateSemanticVersion: DependencyVersion get() = DependencyVersion(candidate.version)

    private fun ComponentSelectionWithCurrent.wouldDowngradeVersion(): Boolean = currentSemanticVersion > candidateSemanticVersion
    private fun ComponentSelectionWithCurrent.wouldDestabilizeAStableVersion(): Boolean = currentSemanticVersion.isStable && !candidateSemanticVersion.isStable
}