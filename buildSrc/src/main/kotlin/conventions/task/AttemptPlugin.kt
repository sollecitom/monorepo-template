package conventions.task

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import org.gradle.kotlin.dsl.create

abstract class AttemptPluginExtension {

    abstract val cool: Property<Boolean>

    @get:Nested
    abstract val author: AuthorData

    fun author(action: Action<AuthorData>) {
        action.execute(author)
    }
}

abstract class AuthorData {

    abstract val firstName: Property<String>
    abstract val lastName: Property<String>
}

abstract class AttemptPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val extension = project.extensions.create<AttemptPluginExtension>("attemptPlugin")
        project.afterEvaluate {
            val cool = extension.cool.convention(false).get()
            val firstName = extension.author.firstName.convention("Michele").get()
            val lastName = extension.author.lastName.convention("Sollecito").get()
            project.logger.quiet("$firstName $lastName ${if (cool) "is" else "isn't"} cool")
        }
    }
}