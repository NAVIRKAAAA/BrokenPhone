plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.brokentelephone.game.features.profile"
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
    implementation(project(":features:bottom_nav_bar"))
    implementation(project(":features:profile_api"))
    implementation(project(":features:chain_details_api"))
    implementation(project(":features:sign_in_api"))
    implementation(project(":features:sign_up_api"))
    implementation(project(":features:edit_profile_api"))
    implementation(project(":features:settings_api"))
    implementation(project(":features:friends_api"))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose.viewmodel)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
