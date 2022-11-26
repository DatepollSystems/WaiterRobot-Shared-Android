plugins {
    id("com.android.application")
    kotlin("android")
}

version = Versions.androidAppVersionName

android {
    namespace = "org.datepollsystems.waiterrobot.android"
    compileSdk = Versions.androidCompileSdk
    defaultConfig {
        applicationId = "org.datepollsystems.waiterrobot.android"
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk
        versionCode = Versions.androidAppVersionCode
        versionName = Versions.androidAppVersionName
        buildToolsVersion = Versions.androidBuildTools
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }
}

dependencies {
    implementation(project(":shared"))

    implementation("androidx.lifecycle:lifecycle-process:${Versions.androidxLifecycle}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidxLifecycle}")
    implementation("androidx.appcompat:appcompat:1.5.1")

    // Update to version 2.0.0 requires AGP (Android Gradle Plugin) version > 7.4.0-alpha10. No stable release yet and also currently not compatible with KMM
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.2")

    // Compose
    runtimeOnly("androidx.compose.compiler:compiler:${Versions.composeCompiler}")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.foundation:foundation:${Versions.compose}")
    implementation("androidx.compose.foundation:foundation-layout:${Versions.compose}")
    implementation("androidx.compose.ui:ui-graphics:${Versions.compose}")
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.material:material-icons-core:${Versions.compose}")
    implementation("androidx.compose.material:material-icons-extended:${Versions.compose}")

    // Compose helpers
    implementation("com.google.accompanist:accompanist-swiperefresh:0.25.1")

    // Architecture (MVI)
    implementation("org.orbit-mvi:orbit-compose:${Versions.orbitMvi}")

    // Dependency injection
    implementation("io.insert-koin:koin-androidx-compose:${Versions.koinDi}")
}
