plugins {
    id("com.android.application")
}

//test

android {
    namespace = "com.example.antismokingassistant"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.antismokingassistant"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.exifinterface:exifinterface:1.3.6")
    implementation("org.tensorflow:tensorflow-lite-task-audio:0.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    //권한 받기
//    implementation("gun0912.ted:tedpermission:3.3.0")
//    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")
//    implementation
//    implementation ("androidx.exifinterface:exifinterface:1.3.3'")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}