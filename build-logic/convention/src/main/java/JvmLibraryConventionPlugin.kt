import com.kotlinpl.convention.configureJacocoJvm
import com.kotlinpl.convention.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import com.kotlinpl.convention.libs
import org.gradle.kotlin.dsl.dependencies

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.jvm")

            configureKotlinJvm()

            dependencies {
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("mockito-core").get())
                "testImplementation"(libs.findLibrary("mockito-kotlin").get())
                "testImplementation"(libs.findLibrary("junit").get())
            }
        }
    }
}