plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.material3)
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")

    implementation(project(":shared-date"))
    implementation(project(":shared-group"))
    implementation(project(":shared-schedule"))
    implementation(project(":shared-ui"))
    implementation(project(":libs-navigation"))
    implementation("com.google.firebase:firebase-messaging:23.4.1")
}

android {
    kotlinOptions {
        jvmTarget = "11"
    }

    namespace = "com.example.schedule.feature.schedule"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}