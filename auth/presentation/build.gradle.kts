plugins {
    alias(libs.plugins.travelinandroid.android.library.compose)
    alias(libs.plugins.travelinandroid.android.hilt)
}

android {
    namespace = "com.projectlab.auth.presentation"
}

dependencies {

    // Core System
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)

    // Material Icons
    implementation(libs.androidx.compose.material.icons)

}