import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.color_match3"
    compileSdk = 34

    defaultConfig {
        buildConfigField("String", "OPENAI_API_KEY", "\"${getApiKey()}\"")
        applicationId = "com.example.color_match3"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
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


    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation (libs.glide)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

fun getApiKey(): String {
    val apikeyPropertiesFile = rootProject.file("apikey.properties")
    if (!apikeyPropertiesFile.exists()) {
        error("apikey.properties file not found. Please create it in the root project directory.")
    }

    val properties = apikeyPropertiesFile.readLines().associate { line ->
        line.split("=").let { it[0].trim() to it[1].trim() }
    }

    return properties["OPENAI_API_KEY"] ?: error("OPENAI_API_KEY not found in apikey.properties file.")
}
