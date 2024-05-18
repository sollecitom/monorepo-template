import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.apply {
        jvmTarget = KotlinConfiguration.TargetJvm.version
        javaParameters = KotlinConfiguration.Compiler.generateJavaParameters
        freeCompilerArgs = KotlinConfiguration.Compiler.arguments
    }
}

tasks.withType<KotlinCompile>()

println("MICHELE!")