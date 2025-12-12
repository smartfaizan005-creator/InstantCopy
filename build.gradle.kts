plugins {
    id("com.android.library") version "8.1.0"
    kotlin("android") version "1.9.0"
    kotlin("kapt") version "1.9.0"
}

android {
    namespace = "com.example.textselection"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        aidl = false
    }
}

dependencies {
    // AndroidX
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.5.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("org.robolectric:robolectric:4.11.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("org.mockito:mockito-android:5.5.1")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
}
