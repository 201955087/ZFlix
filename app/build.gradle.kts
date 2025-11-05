import java.util.Properties

// `local.properties` 파일에서 값을 읽어옵니다
val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
val apiBaseUrl = localProperties.getProperty("withcorn_Zflix_url")
val geminiApiKey = localProperties.getProperty("GEMINI_API_KEY")

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
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

//        val apiBaseUrl: String = if (project.hasProperty("withcorn_Zflix_url")) {
//            project.property("withcorn_Zflix_url") as String
//        } else {
//            "https://default.com/"
//        }
        buildConfigField("String", "withcorn_Zflix_url", "\"${apiBaseUrl}\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"${geminiApiKey}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            // 기존에 추가했던 설정
            pickFirsts.add("META-INF/INDEX.LIST")

            // ⭐ 새로운 충돌 파일 추가
            pickFirsts.add("META-INF/DEPENDENCIES")
        }
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
//    implementation(libs.generativeai)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // (이전에 추가했던 개별 exclude 구문은 모두 삭제된 상태)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.google.code.gson:gson:2.10.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.2.0"))

    // 2. 모든 Firebase 라이브러리에서 버전 번호를 제거하세요!
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    // (여기에 있던 io.grpc에도 개별 exclude가 있었다면 모두 제거)
    implementation("io.grpc:grpc-okhttp:1.64.0")

    // gemini
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

//    implementation("com.google.ai.client.generativeai:generativeai:2.0.0")
    // 코루틴
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}