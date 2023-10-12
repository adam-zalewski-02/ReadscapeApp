@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.database"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(projects.core.model)
    implementation(libs.androidx.test.core.ktx)
    implementation(libs.androidx.test.ext.junit.ktx)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)


    //room
    implementation(libs.room.ktx)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    testImplementation(libs.room.testing)
    ksp(libs.room.compiler)
    implementation(libs.kotlinx.serialization.json)
    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.androidx.test.rules)

}