package conventions.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private val optIns = listOf("kotlin.Experimental", "kotlinx.coroutines.ExperimentalCoroutinesApi")
private val optInCompilerArguments = optIns.map { "-opt-in=$it" }
private val compilerArgs = optInCompilerArguments + listOf("-Xcontext-receivers")
private val targetJvmVersion = "21"

class KotlinConventions : Plugin<Project> {

    override fun apply(project: Project) {

        project.tasks.withType<KotlinCompile>().configureEach {

            kotlinOptions.apply {
                jvmTarget = targetJvmVersion
                javaParameters = true
                freeCompilerArgs = compilerArgs
            }
        }
    }
}
