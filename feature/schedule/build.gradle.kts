plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.material3)
    implementation(libs.accompanist.pager)

    implementation(project(":shared-date"))
    implementation(project(":shared-group"))
    implementation(project(":shared-schedule"))
    implementation(project(":libs-navigation"))
}

android {
    kotlinOptions {
        jvmTarget = "11"
    }

    namespace = "com.example.schedule"
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