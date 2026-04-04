plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)

    id ("app.cash.paparazzi") version "2.0.0-alpha04"
}

android {
    namespace = "com.brokentelephone.game"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.brokentelephone.game"
        minSdk = 24
        targetSdk = 36
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":core"))
    implementation(project(":essentials"))
    implementation(project(":domain"))

    implementation(project(":features:welcome"))
    implementation(project(":features:welcome_api"))

    implementation(project(":features:sign_up"))
    implementation(project(":features:sign_up_api"))

    implementation(project(":features:sign_in"))
    implementation(project(":features:sign_in_api"))

    implementation(project(":features:choose_avatar"))
    implementation(project(":features:choose_avatar_api"))

    implementation(project(":features:forgot_password"))
    implementation(project(":features:forgot_password_api"))

    implementation(project(":features:choose_username"))
    implementation(project(":features:choose_username_api"))

    implementation(project(":features:dashboard"))
    implementation(project(":features:dashboard_api"))

    implementation(project(":features:bottom_nav_bar"))

    implementation(project(":features:profile"))
    implementation(project(":features:profile_api"))

    implementation(project(":features:post_details"))
    implementation(project(":features:post_details_api"))

    implementation(project(":features:draw"))
    implementation(project(":features:draw_api"))

    implementation(project(":features:describe_drawing"))
    implementation(project(":features:describe_drawing_api"))

    implementation(project(":features:chain_details"))
    implementation(project(":features:chain_details_api"))

    implementation(project(":features:edit_profile"))
    implementation(project(":features:edit_profile_api"))

    implementation(project(":features:edit_avatar"))
    implementation(project(":features:edit_avatar_api"))

    implementation(project(":features:edit_username"))
    implementation(project(":features:edit_username_api"))

    implementation(project(":features:edit_bio"))
    implementation(project(":features:edit_bio_api"))

    implementation(project(":features:edit_email"))
    implementation(project(":features:edit_email_api"))

    implementation(project(":features:user_details"))
    implementation(project(":features:friends"))
    implementation(project(":features:user_friends"))
    implementation(project(":features:add_friend"))
    implementation(project(":features:notifications"))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.startup)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.paparazzi)
}