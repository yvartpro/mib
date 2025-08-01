plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  kotlin("plugin.serialization") version "2.0.20"
  kotlin("kapt")
  id("com.google.dagger.hilt.android")

}

android {
  namespace = "bi.vovota.madeinburundi"
  compileSdk = 35

  defaultConfig {
    applicationId = "bi.vovota.madeinburundi"
    minSdk = 21
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    compose = true
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  //accompanist compose
  implementation(libs.accompanist.placeholder.material)

  //custom dependencies
  implementation("com.google.accompanist:accompanist-flowlayout:0.34.0")

  // Coroutines & Serialization
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

  // Ktor
  implementation("io.ktor:ktor-client-core:2.3.5")
  implementation("io.ktor:ktor-client-cio:2.3.5")
  implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
  implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
  implementation("io.ktor:ktor-client-auth:2.3.5")

  //dataStore
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  //coi for images
  implementation("io.coil-kt:coil-compose:2.4.0")
  //implementation("com.github.bumptech.glide:glide:4.15.1")
  // Navigation
  implementation("androidx.navigation:navigation-compose:2.6.0")
  //viewmodel
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
  //Hilt
  implementation("com.google.dagger:hilt-android:2.50")
  kapt("com.google.dagger:hilt-compiler:2.50")
  implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
  //splash theme
  //implementation("androidx.core:core-splashscreen:1.0.1")
}