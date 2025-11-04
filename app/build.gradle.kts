import java.util.Properties

// `local.properties` 파일에서 값을 읽어옵니다
val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
val apiBaseUrl = localProperties.getProperty("withcorn_Zflix_url")


plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.kyl.zflix"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.kyl.zflix"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        val apiBaseUrl: String = if (project.hasProperty("withcorn_Zflix_url")) {
            project.property("withcorn_Zflix_url") as String
        } else {
            "https://default.com/"
        }
        buildConfigField("String", "withcorn_Zflix_url", "\"${apiBaseUrl}\"")

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.google.code.gson:gson:2.10.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.2.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore:24.11.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
