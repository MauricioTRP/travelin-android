import com.android.build.api.dsl.LibraryExtension
import com.kotlinpl.convention.ExtensionType
import com.kotlinpl.convention.configureBuildTypes
import com.kotlinpl.convention.configureKotlinAndroid
import com.kotlinpl.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "android-library")
            apply(plugin = "kotlin-android")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig {
                    minSdk = 26

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            dependencies {
                "implementation"(libs.findLibrary("androidx-core-ktx").get())
                "implementation"(libs.findLibrary("androidx-appcompat").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("androidx-test-core").get())
                "testImplementation"(libs.findLibrary("androidx-test-core-ktx").get())
                "testImplementation"(libs.findLibrary("mockito-core").get())
                "testImplementation"(libs.findLibrary("mockito-kotlin").get())
                "androidTestImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "androidTestImplementation"(libs.findLibrary("androidx-test-core").get())
                "androidTestImplementation"(libs.findLibrary("androidx-test-core-ktx").get())
                "androidTestImplementation"(libs.findLibrary("androidx-junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx-test-rules").get())
                "androidTestImplementation"(libs.findLibrary("androidx-espresso.core").get())
                "androidTestImplementation"(libs.findLibrary("mockito-android").get())
            }
        }
    }
}