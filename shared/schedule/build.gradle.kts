plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    kotlinOptions {
        jvmTarget = "11"
    }
    namespace = "com.example.schedule.shared.schedule"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}