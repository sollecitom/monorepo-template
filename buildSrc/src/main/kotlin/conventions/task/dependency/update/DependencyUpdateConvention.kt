package conventions.task.dependency.update

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent
import nl.littlerobots.vcu.plugin.*
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType

abstract class DependencyUpdateConvention : Plugin<Project> {

    override fun apply(project: Project) = with(project) {

        pluginManager.apply(VersionCatalogUpdatePlugin::class)
        pluginManager.apply(VersionsPlugin::class)
        val extension = project.extensions.create<Extension>("versionCatalog")

        afterEvaluate {
            tasks.withType<DependencyUpdatesTask> { // TODO make these configurable through the extension
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
                sortByKey.set(extension.sortByKey.getOrElse(defaultSortByKey))
                extension.catalogFile.takeIf { it.isPresent }?.let {
                    catalogFile.set(it.get())
                }
                keep {
                    keepUnusedVersions.set(extension.keep.keepUnusedVersions.getOrElse(defaultKeepUnusedVersions))
                    keepUnusedLibraries.set(extension.keep.keepUnusedLibraries.getOrElse(defaultKeepUnusedLibraries))
                    keepUnusedPlugins.set(extension.keep.keepUnusedPlugins.getOrElse(defaultKeepUnusedPlugins))
                    versions.set(extension.keep.versions.getOrElse(emptySet()))
                    groups.set(extension.keep.groups.getOrElse(emptySet()))
                    libraries.set(extension.keep.libraries.getOrElse(emptySet()))
                    plugins.set(extension.keep.plugins.getOrElse(emptySet()))
                }
                pin {
                    versions.set(extension.pins.versions.getOrElse(emptySet()))
                    groups.set(extension.pins.groups.getOrElse(emptySet()))
                    libraries.set(extension.pins.libraries.getOrElse(emptySet()))
                    plugins.set(extension.pins.plugins.getOrElse(emptySet()))
                }
            }
        }
    }

    private companion object {
        const val defaultSortByKey = false
        const val defaultKeepUnusedVersions = true
        const val defaultKeepUnusedLibraries = true
        const val defaultKeepUnusedPlugins = true
    }

    abstract class Extension {

        @get:Optional
        abstract val sortByKey: Property<Boolean>

        @get:Optional
        abstract val catalogFile: RegularFileProperty

        @get:Nested
        abstract val keep: KeepConfiguration

        @get:Nested
        abstract val pins: PinConfiguration

        @get:Nested
        abstract val versionCatalogs: NamedDomainObjectContainer<VersionCatalogConfig>

        fun keep(action: Action<KeepConfiguration>) = action.execute(keep)

        fun pins(action: Action<PinConfiguration>) = action.execute(pins)

        fun versionCatalogs(action: Action<NamedDomainObjectContainer<VersionCatalogConfig>>) = action.execute(versionCatalogs)
    }

    private val ComponentSelectionWithCurrent.currentSemanticVersion: DependencyVersion get() = DependencyVersion(currentVersion)
    private val ComponentSelectionWithCurrent.candidateSemanticVersion: DependencyVersion get() = DependencyVersion(candidate.version)

    private fun ComponentSelectionWithCurrent.wouldDowngradeVersion(): Boolean = currentSemanticVersion > candidateSemanticVersion
    private fun ComponentSelectionWithCurrent.wouldDestabilizeAStableVersion(): Boolean = currentSemanticVersion.isStable && !candidateSemanticVersion.isStable
}