plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.kamusaga_2023606601031"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kamusaga_2023606601031"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation (libs.appcompat.v161)
    implementation (libs.material.v190)
    implementation (libs.constraintlayout.v214)
    implementation (libs.recyclerview)

    // Retrofit & JSON Parsing (Gson)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // OkHttp Logging Interceptor (untuk debugging)
    implementation (libs.logging.interceptor)

    // Optional: Lifecycle support (jika kamu butuh)
    implementation (libs.lifecycle.runtime.ktx)

    // Testing dependencies (opsional)
    testImplementation (libs.junit)
    androidTestImplementation (libs.junit.v115)
    androidTestImplementation (libs.espresso.core.v351)
}
