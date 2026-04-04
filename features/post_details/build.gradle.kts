plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.brokentelephone.game.features.post_details"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":essentials"))
    implementation(project(":features:post_details_api"))
    implementation(project(":features:dashboard_api"))
    implementation(project(":features:draw_api"))
    implementation(project(":features:describe_drawing_api"))
    implementation(project(":features:chain_details_api"))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose.viewmodel)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
