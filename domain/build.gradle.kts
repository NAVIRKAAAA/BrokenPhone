plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.brokentelephone.game.domain"
    compileSdk = 37

    defaultConfig {
        minSdk = 27
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":essentials"))
    implementation(libs.androidx.core.ktx)
    api(platform(libs.firebase.bom))
    api(libs.kotlinx.coroutines.core)
}