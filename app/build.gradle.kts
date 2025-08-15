plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.newsevents"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.newsevents"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { compose = true }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    // --- Compose BOM ---
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))

    // --- Compose UI ---
    implementation(libs.bundles.compose.core)
    debugImplementation(libs.compose.ui.tooling)

    // --- Kotlin/Coroutines/Lifecycle ---
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)

    // --- Hilt ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // --- Networking ---
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.networking)
    implementation(libs.serialization.json)

    implementation(libs.bundles.room)     // 2.7.2
    ksp(libs.room.compiler)

    // --- DataStore, Images, Custom Tabs, Splash ---
    implementation(libs.datastore.preferences)
    implementation(libs.browser)
    implementation(libs.splashscreen)

    // COIL v3 (new imports: coil3.*)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // --- Tests ---
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation("androidx.compose.material3:material3:1.3.0-beta04")
    implementation("androidx.compose.material:material:1.8.3")

    // --- Paging + Room (stable only) ---
//    implementation(libs.bundles.paging)   // 3.3.6
    // optional - Jetpack Compose integration

    implementation ("androidx.paging:paging-compose:3.3.6")
    implementation ("androidx.paging:paging-runtime:3.3.6")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2")

    implementation("com.google.android.material:material:1.12.0")

}
