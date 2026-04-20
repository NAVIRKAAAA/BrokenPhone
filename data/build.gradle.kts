import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

android {
    namespace = "com.brokentelephone.game.data"
    compileSdk = 37

    defaultConfig {
        minSdk = 27

        buildConfigField("String", "WEB_CLIENT_ID", "\"${localProperties["WEB_CLIENT_ID"]}\"")
        buildConfigField("String", "SUPABASE_URL", "\"${localProperties["SUPABASE_URL"]}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${localProperties["SUPABASE_ANON_KEY"]}\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":essentials"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.koin.android)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.googleid)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.startup)
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.realtime)
    implementation(libs.supabase.storage)
    implementation(libs.ktor.client.okhttp)
}
