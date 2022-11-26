import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("co.touchlab.faktory.kmmbridge") version "0.3.2"
}

// keep patch always on 0 for shared module (patch is managed by kmmbridge for spm releases)
version = "0.1.0"

kotlin {
    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // needed to export kotlin documentation in objective-c headers
    targets.withType<KotlinNativeTarget> {
        compilations["main"].kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Dependency injection
                implementation("io.insert-koin:koin-core:${Versions.koinDi}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                // Dependency injection
                api("io.insert-koin:koin-android:${Versions.koinDi}")
            }
        }
        val androidTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }

    // Needed for kmmbrigde to create swift package
    cocoapods {
        name = "shared"
        summary = "Shared KMM iOS-module of the WaiterRobot app"
        homepage = "https://github.com/DatepollSystems/waiterrobot-mobile_android-shared"
        authors = "DatepollSystems"
        ios.deploymentTarget = "15"

        framework {
            isStatic = true
        }
    }
}

android {
    namespace = "org.datepollsystems.waiterrobot"
    compileSdk = Versions.androidCompileSdk
    defaultConfig {
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk
    }
}

kmmbridge {
    githubReleaseArtifacts()
    githubReleaseVersions()
    spm()
    // Remove patch as this will be set by the GitHubReleaseVersion manager
    versionPrefix.set((version as String).substringBeforeLast("."))
}
