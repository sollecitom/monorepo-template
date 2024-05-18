package conventions.task.test

import JvmConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.withType

class TestTaskConventions : Plugin<Project> {

    override fun apply(project: Project) {

        project.tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            if (isRunningOnRemoteBuildEnvironment()) {
                maxParallelForks = 1
                maxHeapSize = "1g"
            } else {
                maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
            }
            testLogging {
                showStandardStreams = false
                exceptionFormat = TestExceptionFormat.FULL
            }
            jvmArgs = JvmConfiguration.testArgs
        }
    }

    private fun isRunningOnRemoteBuildEnvironment() = System.getenv("CI") != null
}