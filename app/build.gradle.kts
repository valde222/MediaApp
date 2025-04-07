plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.compose")
}


android {
    namespace = "com.example.mediaapp"
    compileSdk = 34
    //android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.example.mediaapp"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type. Make sure to use a build
            // variant with `isDebuggable=false`.
            isMinifyEnabled = false

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = false

            // Includes the default ProGuard rules files that are packaged with
            // the Android Gradle plugin. To learn more, go to the section about
            // R8 configuration files.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true

    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "34.0.0"

}




dependencies {

    // Coroutines - Use the latest stable (e.g., 1.8.0 or 1.8.1) , remove duplicates
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Coil - Use the latest stable, remove duplicate
    implementation("io.coil-kt:coil-compose:2.6.0") // Check for latest coil version

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // 2.11.0 is latest stable
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0") // 2.11.0 is latest stable
    implementation(platform("androidx.compose:compose-bom:2025.03.01"))
    // ViewModel / Lifecycle Compose - Use versions from BOM or check latest
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0") // Or rely on BOM
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0") // Or rely on BOM

    // Navigation - Use versions from BOM or check latest
    implementation("androidx.navigation:navigation-compose:2.7.7") // Or rely on BOM
    implementation("androidx.activity:activity-compose:1.9.0") // Or rely on BOM

    // Core KTX - Use versions from BOM or check latest
    implementation("androidx.core:core-ktx:1.13.1") // Or rely on BOM

    // Compose BOM - Use a RECENT version
    // Check for the latest BOM here: https://developer.android.com/jetpack/androidx/releases/compose-bom
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.1") // Material 3 is recommended
    implementation("androidx.compose.material:material-icons-extended")
    // Remove Wear Compose unless this is a Wear OS app? These look out of place.
    // implementation("androidx.wear.compose:compose-material:1.4.1")
    // implementation("androidx.wear.compose:compose-material3:1.0.0-alpha35")

    // Firebase (check for latest versions if desired)
    implementation("com.google.firebase:firebase-auth:23.0.0") // Check latest
    implementation("com.google.firebase:firebase-firestore:25.0.0") // Check latest

    // Supabase (check for latest versions)
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.3")) // Check latest bom version
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt:3.1.3")
    implementation("io.github.jan-tennert.supabase:realtime-kt")

    // Other dependencies (check versions if needed)
    implementation("com.google.android.engage:engage-core:1.5.7")
    implementation("com.github.a914-gowtham:compose-ratingbar:1.3.4")

    // Test dependencies (usually updated via BOM)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // or 1.2.0
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // or 3.6.0
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00")) // Match app BOM
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    val koinVersion = "3.5.6"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
    implementation("io.insert-koin:koin-androidx-viewmodel:$koinVersion")
}