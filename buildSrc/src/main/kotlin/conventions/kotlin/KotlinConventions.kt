package conventions.kotlin

import KotlinConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinConventions : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions.apply {
                jvmTarget = KotlinConfiguration.TargetJvm.version
                javaParameters = KotlinConfiguration.Compiler.generateJavaParameters
                freeCompilerArgs = KotlinConfiguration.Compiler.arguments
            }
        }
    }
}
