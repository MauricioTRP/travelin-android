plugins {
    alias(libs.plugins.travelinandroid.android.library.compose)
    alias(libs.plugins.travelinandroid.android.hilt)
    alias(libs.plugins.travelinandroid.android.library.jacoco)
}

android {
    namespace = "com.projectlab.booking.presentation"

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlin.get()
    }
}

dependencies {
    // Core System
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.domain)
    implementation(projects.core.data)

    // Dagger Hilt + Ksp
    implementation(libs.hilt.navigation)

    // Google services: Firebase products
    implementation(platform(libs.firebase))
    implementation(libs.firebase.firestore)

    // Jetpack Compose
    // This dependency must be on :APP module
    implementation(libs.androidx.navigation.compose)

    // UI
    implementation(libs.coil.compose)

    // Proto DataStore
    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.javalite)
    // Testing
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.android)
    testImplementation(libs.kotlinx.coroutines.test)
}