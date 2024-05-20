import com.palantir.gradle.gitversion.GitVersionPlugin
import com.palantir.gradle.gitversion.VersionDetails
import conventions.task.AttemptPlugin
import conventions.task.dependency.update.DependencyUpdateConvention
import conventions.task.kotlin.KotlinTaskConventions
import conventions.task.maven.publish.MavenPublishConvention
import conventions.task.test.TestTaskConventions
import groovy.lang.Closure
import java.nio.file.Path
import java.nio.file.Paths

buildscript { repositories { RepositoryConfiguration.BuildScript.apply(this) } }

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    idea
    alias(libs.plugins.com.palantir.git.version)
    alias(libs.plugins.jib) apply false
}

apply<GitVersionPlugin>()
apply<AttemptPlugin>()

configure<AttemptPlugin.Extension> {
    cool = true
    author {
        firstName = "Bruce"
        lastName = "Wayne"
    }
}

val parentProject = this
val currentVersion: String by project
val versionCatalogName: String by project
val versionDetails: Closure<VersionDetails> by extra
val gitVersion = versionDetails()
val libsFolder: Path = rootProject.projectDir.path.let { Paths.get(it) }.resolve("libs")
val servicesFolder: Path = rootProject.projectDir.path.let { Paths.get(it) }.resolve("services")
val toolsFolder: Path = rootProject.projectDir.path.let { Paths.get(it) }.resolve("tools")

fun Project.isLibrary() = projectDir.path.let { Paths.get(it) }.startsWith(libsFolder)

allprojects {

    group = ProjectSettings.groupId
    version = currentVersion

    repositories { RepositoryConfiguration.Modules.apply(this, project) }

    apply<IdeaPlugin>()
    idea { module { inheritOutputDirs = true } }

    apply<KotlinTaskConventions>()
    apply<TestTaskConventions>()

    subprojects {
        apply {
            plugin("org.jetbrains.kotlin.jvm")
            plugin<JavaLibraryPlugin>()
        }

        java(Plugins.JavaPlugin::configure)
    }

    if (isLibrary()) {
        apply<MavenPublishConvention>()
    }
}

apply<DependencyUpdateConvention>()

val containerBasedServiceTest: Task = tasks.register("containerBasedServiceTest").get()