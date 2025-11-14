plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.solarexplorer"
    compileSdk = 36 // 36 not available yet in stable version; 34 is safer

    defaultConfig {
        applicationId = "com.example.solarexplorer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    // Android core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    //implementation(libs.androidx.compose.material)
    implementation("androidx.compose.material3:material3") // ADD THIS LINE


    // Navigation in Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ViewModel + Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Lottie for animations
    implementation("com.airbnb.android:lottie-compose:6.1.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.ads.mobile.sdk)


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Lottie for Compose (animation)
    implementation("com.airbnb.android:lottie-compose:6.6.10")
// (from Maven Central). :contentReference[oaicite:0]{index=0}
// Coil for image loading in Compose (optional, useful for remote images)
    implementation("io.coil-kt:coil-compose:2.7.0")
// Composer-friendly Coil. :contentReference[oaicite:1]{index=1}

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0")


}
