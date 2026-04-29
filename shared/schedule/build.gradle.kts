plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

dependencies {
    implementation(libs.koin.android)
    // Retrofit (Сетевые запросы)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Конвертер JSON в Kotlin-объекты
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Логирование запросов (чтобы видеть в Logcat, что качается)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}

android {
    kotlinOptions {
        jvmTarget = "11"
    }
    namespace = "com.example.schedule.shared.schedule"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}