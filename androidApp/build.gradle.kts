import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
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

    signingConfigs {
        val key: String? = gradleLocalProperties(rootDir).getProperty("keyPassword", null)
        val keyStoreFile = file(".keys/app_sign.jks")
        if (key != null && keyStoreFile.exists()) { // Allow build also when signing key is not defined
            create("release") {
                keyAlias = "WaiterRobot"
                storeFile = keyStoreFile
                keyPassword = key
                storePassword = key
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }

        release {
            isMinifyEnabled = false // TODO enable proguard
            signingConfig = signingConfigs.findByName("release")
            ndk.debugSymbolLevel =
                com.android.build.gradle.internal.dsl.NdkOptions.DebugSymbolLevel.FULL.name
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

    flavorDimensions += "environment"

    productFlavors {
        create("lava") {
            dimension = "environment"
            applicationIdSuffix = ".lava"
            resValue("string", "app_name", "WaiterRobot Lava")
            buildConfigField("String", "API_BASE", "\"https://lava.kellner.team/api\"")
            manifestPlaceholders["host"] = "lava.kellner.team"
        }
        create("prod") {
            dimension = "environment"
            resValue("string", "app_name", "WaiterRobot")
            buildConfigField("String", "API_BASE", "\"https://my.kellner.team/api\"")
            manifestPlaceholders["host"] = "my.kellner.team"
        }
    }

    androidComponents {
        beforeVariants { variantBuilder ->
            // Hide lavaRelease
            if (variantBuilder.buildType == "release" && variantBuilder.flavorName == "lava") {
                variantBuilder.enable = false
            }
        }
    }

    // Include the generated navigation sources
    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("${project.buildDir}/generated/ksp/${name}/kotlin")
            }
        }
    }
}

ksp {
    arg(
        "compose-destinations.codeGenPackageName",
        "org.datepollsystems.waiterrobot.android.generated.navigation"
    )
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
    implementation("com.google.accompanist:accompanist-permissions:0.25.1")

    // Architecture (MVI)
    implementation("org.orbit-mvi:orbit-compose:${Versions.orbitMvi}")

    // Dependency injection
    implementation("io.insert-koin:koin-androidx-compose:${Versions.koinDi}")

    // SafeCompose Navigation Args
    implementation("io.github.raamcosta.compose-destinations:core:${Versions.composeDestinations}")
    ksp("io.github.raamcosta.compose-destinations:ksp:${Versions.composeDestinations}")

    // CameraX
    implementation("androidx.camera:camera-camera2:${Versions.camera}")
    implementation("androidx.camera:camera-view:${Versions.camera}")
    implementation("androidx.camera:camera-lifecycle:${Versions.camera}")

    // QrCode Scanning
    implementation("com.google.mlkit:barcode-scanning:17.0.3")
}
