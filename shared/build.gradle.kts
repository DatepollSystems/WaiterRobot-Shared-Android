import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    `maven-publish`
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.touchlab.kmmbridge)
    alias(libs.plugins.touchlab.skie)
    alias(libs.plugins.realm)
}

val iosFrameworkName = "shared"

group = project.property("SHARED_GROUP") as String
version = project.property(
    if (project.hasProperty("AUTO_VERSION")) "AUTO_VERSION" else "SHARED_BASE_VERSION"
) as String
val sharedNamespace = "$group.shared"

kotlin {
    jvmToolchain(17)

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            // Must be set to false for shared localization (otherwise resources are not available)
            isStatic = false
            freeCompilerArgs += "-Xobjc-generics"
            export(libs.sentry)
        }
    }

    // needed to export kotlin documentation in objective-c headers
    targets.withType<KotlinNativeTarget> {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            freeCompilerArgs.add("-Xexport-kdoc")
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCRefinement")
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }

        commonMain {
            dependencies {
                // Logger
                api(libs.touchlab.kermit)

                // Dependency injection
                implementation(libs.koin.core)

                // Architecture
                api(libs.orbit.core) // MVI
                api(libs.moko.mvvm) // ViewModelScope
                implementation(libs.touchlab.skie.annotations)

                // Permissions
                api(libs.moko.permissions)

                // Ktor (HTTP client)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.encoding)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.serialization.json)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.logging)

                // Realm (Database)
                implementation(libs.realm)

                // SharedSettings
                implementation(libs.settings)
                implementation(libs.settings.coroutines)

                // Helper
                api(libs.kotlinx.datetime)
                // Also needed by android for ComposeDestination parameter serialization
                api(libs.kotlinx.serialization.json)

                api(libs.sentry)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        androidMain {
            dependencies {
                // Dependency injection
                api(libs.koin.android)

                // Ktor (HTTP client)
                implementation(libs.ktor.client.cio)
            }
        }

        iosMain {
            dependencies {
                // Ktor (HTTP client)
                implementation(libs.ktor.client.darwin)
            }
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }
}

android {
    namespace = sharedNamespace
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

addGithubPackagesRepository()
kmmbridge {
    mavenPublishArtifacts()
    spm()
}

buildkonfig {
    packageName = "$sharedNamespace.buildkonfig"
    defaultConfigs {
        buildConfigField(Type.STRING, "sharedVersion", version as String, const = true)
    }
}

tasks {
    afterEvaluate {
        // Link the Sentry framework to the iOS targets
        val action = Action<KotlinNativeTarget> target@{
            val frameworkArchitecture = when (name) {
                "iosSimulatorArm64", "iosX64" -> "ios-arm64_x86_64-simulator"
                "iosArm64" -> "ios-arm64"
                else -> {
                    logger.warn("Skipping linking of Sentry for target $name - unsupported architecture.")
                    return@target
                }
            }

            val frameworkPath =
                project.file("Sentry-Dynamic.xcframework/$frameworkArchitecture").absolutePath
            val action = Action<NativeBinary> binary@{
                if (this is TestExecutable) {
                    linkerOpts("-rpath", frameworkPath, "-F$frameworkPath")
                }

                if (this is Framework) {
                    linkerOpts("-F$frameworkPath")
                    logger.info("Linked framework for target ${this@target.name} from $frameworkPath")
                }
            }
            binaries.all(action)
        }
        kotlin.targets.withType<KotlinNativeTarget>()
            .matching { it.konanTarget.family.isAppleFamily }
            .all(action)
    }
}

detekt {
    source.from(
        "src/androidMain/kotlin",
        "src/commonMain/kotlin",
        "src/iosMain/kotlin",
    )
}

skie {
    analytics {
        disableUpload.set(true)
        enabled.set(false)
    }
}
