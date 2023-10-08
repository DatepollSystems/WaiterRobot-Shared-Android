import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("co.touchlab.kmmbridge") version "0.5.0"
    `maven-publish`
    id("dev.jamiecraane.plugins.kmmresources") version "1.0.0-alpha11" // Shared localization
    id("io.realm.kotlin") version "1.10.2"
    id("com.codingfeline.buildkonfig")
}

val generatedLocalizationRoot: String =
    File(project.buildDir, "generated/localizations").absolutePath
val iosFrameworkName = "shared"

group = project.property("SHARED_GROUP") as String
version = project.property(
    if (project.hasProperty("AUTO_VERSION")) "AUTO_VERSION" else "SHARED_BASE_VERSION"
) as String

kotlin {
    // For some reason androidTarget is recognized by IntelliJ,
    // but when building it throws "Unresolved reference: androidTarget"
    // -> Just keep it till it is removed
    android {
        publishAllLibraryVariants()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            // Must be set to false for shared localization (otherwise resources are not available)
            isStatic = false
        }
    }

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
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1") // Also needed by android for ComposeDestination parameter serialization
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
        val androidUnitTest by getting

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

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }
}

android {
    namespace = group as String
    compileSdk = Versions.androidCompileSdk
    defaultConfig {
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk
    }

    // Include the generated localization string resources
    sourceSets["main"].res.srcDir("$generatedLocalizationRoot/androidMain/res")
}

addGithubPackagesRepository()
kmmbridge {
    mavenPublishArtifacts()
    spm()
}

kmmResourcesConfig {
    androidApplicationId.set(group as String) // appId of the shared module
    packageName.set("$group.generated.localization")
    defaultLanguage.set("en")
    input.set(File(project.projectDir, "localization.yml"))
    output.set(project.projectDir)
    srcFolder.set(generatedLocalizationRoot) // place the generated files in the build folder
}

buildkonfig {
    packageName = "$group.buildkonfig"
    defaultConfigs {
        buildConfigField(Type.STRING, "sharedVersion", version as String, const = true)
    }
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
                                "${project.buildDir}/XCFrameworks/${buildType.lowercase()}/" +
                                    "$iosFrameworkName.xcframework/$arch/$iosFrameworkName.framework"
                            )
                        }
                    }
                }
            }
        }
    }

    // Make sure that the localizations are always up to date
    named("preBuild") {
        dependsOn(named("generateLocalizations"))
    }
}

detekt {
    source.from(
        "src/androidMain/kotlin",
        "src/commonMain/kotlin",
        "src/iosMain/kotlin",
    )
}
