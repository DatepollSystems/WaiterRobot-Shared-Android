import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("co.touchlab.faktory.kmmbridge") version "0.3.2"
    id("dev.jamiecraane.plugins.kmmresources") version "1.0.0-alpha10" // Shared localization
    id("io.realm.kotlin") version "1.5.2"
}

// keep patch always on 0 for shared module (patch is managed by kmmbridge for spm releases)
// TODO rework versioning (major -> common prefix, minor -> shared version, patch -> app version -> shared always 0)
version = "0.1.0"

val sharedNamespace = "org.datepollsystems.waiterrobot.shared"
val generatedLocalizationRoot: String =
    File(project.buildDir, "generated/localizations").absolutePath
val iosFrameworkName = "shared"

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
            // Include the generated localization source
            kotlin.srcDir("$generatedLocalizationRoot/commonMain/kotlin")

            dependencies {
                // Logger
                api("co.touchlab:kermit:${Versions.kermitLogger}")

                // Dependency injection
                implementation("io.insert-koin:koin-core:${Versions.koinDi}")

                // Architecture
                api("org.orbit-mvi:orbit-core:${Versions.orbitMvi}") // MVI
                api("dev.icerock.moko:mvvm-core:${Versions.mokoMvvm}") // ViewModelScope

                // Ktor (HTTP client)
                implementation("io.ktor:ktor-client-core:${Versions.ktor}")
                implementation("io.ktor:ktor-client-content-negotiation:${Versions.ktor}")
                implementation("io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}")
                implementation("io.ktor:ktor-client-auth:${Versions.ktor}")
                implementation("io.ktor:ktor-client-logging:${Versions.ktor}")

                // Realm (Database)
                implementation("io.realm.kotlin:library-base:${Versions.realm}")

                // SharedSettings
                implementation("com.russhwolf:multiplatform-settings:${Versions.settings}")
                implementation("com.russhwolf:multiplatform-settings-coroutines:${Versions.settings}")

                // Helper
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1") // Also needed by android for ComposeDestination parameter serialization
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            // Include the generated localization source
            kotlin.srcDir("$generatedLocalizationRoot/androidMain/kotlin")

            dependencies {
                // Dependency injection
                api("io.insert-koin:koin-android:${Versions.koinDi}")

                // Ktor (HTTP client)
                implementation("io.ktor:ktor-client-cio:${Versions.ktor}")
            }
        }
        val androidTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            // Include the generated localization source
            kotlin.srcDir("$generatedLocalizationRoot/iosMain/kotlin")

            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                // Ktor (HTTP client)
                implementation("io.ktor:ktor-client-darwin:${Versions.ktor}")
            }
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
        name = iosFrameworkName
        summary = "Shared KMM iOS-module of the WaiterRobot app"
        homepage = "https://github.com/DatepollSystems/waiterrobot-mobile_android-shared"
        authors = "DatepollSystems"
        ios.deploymentTarget = "15"

        framework {
            // Must be set to false for shared localization (otherwise resources are not available)
            isStatic = false
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }
}

android {
    namespace = sharedNamespace
    compileSdk = Versions.androidCompileSdk
    defaultConfig {
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk
    }

    // Include the generated localization string resources
    sourceSets["main"].res.srcDir("$generatedLocalizationRoot/androidMain/res")
}

kmmbridge {
    githubReleaseArtifacts()
    githubReleaseVersions()
    spm()
    // Remove patch as this will be set by the GitHubReleaseVersion manager
    versionPrefix.set((version as String).substringBeforeLast("."))
}

kmmResourcesConfig {
    androidApplicationId.set(sharedNamespace) // appId of the shared module
    packageName.set("${sharedNamespace}.generated.localization")
    defaultLanguage.set("en")
    input.set(File(project.projectDir, "localization.yml"))
    output.set(project.projectDir)
    srcFolder.set(generatedLocalizationRoot) // place the generated files in the build folder
}


tasks {
    // Plutil generates the localizations for ios
    val plutil = named("executePlutil") {
        dependsOn(named("generateLocalizations"))
    }

    // Generate the localizations for all ios targets
    listOf("IosX64", "IosArm64", "IosSimulatorArm64").forEach { arch ->
        // Ensure that localizations are up to date on compile
        named("compileKotlin$arch") {
            dependsOn(plutil)
        }
    }

    afterEvaluate {
        // Copy the generated iOS localizations to the framework
        listOf("Release", "Debug").forEach { buildType ->
            named("assembleShared${buildType}XCFramework") {
                doLast {
                    // TODO can we get this names from somewhere?
                    listOf("ios-arm64", "ios-arm64_x86_64-simulator").forEach { arch ->
                        copy {
                            from("$generatedLocalizationRoot/commonMain/resources/ios")
                            into(
                                "${project.buildDir}/XCFrameworks/${buildType.toLowerCase()}/" +
                                    "$iosFrameworkName.xcframework/$arch/$iosFrameworkName.framework"
                            )
                        }
                    }
                }
            }
        }
    }

    // Make sure that there are always up to date localizations
    named("preBuild") {
        dependsOn(named("generateLocalizations"))
    }
}
