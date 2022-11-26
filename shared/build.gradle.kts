import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("co.touchlab.faktory.kmmbridge") version "0.3.2"
    id("dev.jamiecraane.plugins.kmmresources") version "1.0.0-alpha10" // Shared localization
}

// keep patch always on 0 for shared module (patch is managed by kmmbridge for spm releases)
version = "0.1.0"

val sharedNamespace = "org.datepollsystems.waiterrobot.shared"
val generatedLocalizationRoot: String =
    File(project.buildDir, "generated/localizations").absolutePath

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
        named("zipXCFramework") {
            // Copy the generated iOS localizations to the framework before zipping
            doFirst {
                val targetDirectories = file(
                    "${project.buildDir}/XCFrameworks/" +
                        "${kmmbridge.buildType.get().getName()}/" +
                        "${kmmbridge.frameworkName.get()}.xcframework"
                ).listFiles()
                    ?.filter { it.isDirectory }
                    ?.flatMap { it.listFiles()?.toList() ?: emptyList() }
                    ?.filter { it.isDirectory && it.name == "${kmmbridge.frameworkName.get()}.framework" }
                    ?: emptyList()

                copy {
                    from("$generatedLocalizationRoot/commonMain/resources/ios")
                    targetDirectories.forEach { into(it) }
                }
            }
        }
    }

    // Make sure that there are always up to date localizations
    named("preBuild") {
        dependsOn(named("generateLocalizations"))
    }
}
