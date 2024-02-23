import com.android.build.gradle.internal.dsl.NdkOptions.DebugSymbolLevel
import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.play.publisher)
    alias(libs.plugins.google.ksp)
}

private val versionProperty by lazy {
    Properties().apply {
        File(project.projectDir, "version.properties").inputStream().use { load(it) }
    }
}

val SHARED_GROUP: String by project
val SHARED_BASE_VERSION: String by project

version = versionProperty.getProperty("androidVersion")
group = SHARED_GROUP

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "org.datepollsystems.waiterrobot.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    androidResources {
        generateLocaleConfig = true
    }

    defaultConfig {
        applicationId = this@android.namespace

        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        buildToolsVersion = libs.versions.android.buildTools.get()

        versionName = version.toString()
        versionCode = run {
            // Generate VersionCode from VersionName (e.g. 1.2.3 -> 10203, 1.23.45 -> 12345)
            val (major, minor, patch) = versionName!!.split(".").map(String::toInt)
            major * 10_000 + minor * 100 + patch
        }

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        val keyPassword: String? = project.findProperty("keyPassword")?.toString()
        val storePassword: String? = project.findProperty("storePassword")?.toString()
        val keyStoreFile = file(".keys/app_sign.jks")

        // Only create signingConfig, when all needed configs are available
        if (keyPassword != null && storePassword != null && keyStoreFile.exists()) {
            create("release") {
                keyAlias = "WaiterRobot"
                storeFile = keyStoreFile
                this.keyPassword = keyPassword
                this.storePassword = storePassword
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
            ndk.debugSymbolLevel = DebugSymbolLevel.FULL.name
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    flavorDimensions += "environment"

    productFlavors {
        create("lava") {
            dimension = "environment"
            applicationIdSuffix = ".lava"
            buildConfigField("String", "API_BASE", "\"https://lava.kellner.team/api\"")
            manifestPlaceholders["host"] = "lava.kellner.team"

            // Use time-based versionCode for lava to allow multiple build per "base version"
            // versionCode is limited to "2100000000" by google play.
            // If using epochSeconds this would overflow in 2036.
            // -> use epochMinutes (overflow would be in 5962).
            // (conversion to int is save as java int is bigger as the max versionCode allowed by google play)
            val epochMinutes = (Date().toInstant().epochSecond / 60).toInt()
            versionNameSuffix = "-lava-$epochMinutes"
            versionCode = epochMinutes
        }

        create("prod") {
            dimension = "environment"
            buildConfigField("String", "API_BASE", "\"https://my.kellner.team/api\"")
            manifestPlaceholders["host"] = "my.kellner.team"
        }
    }

    applicationVariants.all variant@{
        // Include the generated navigation sources
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir(File(project.layout.buildDirectory.asFile.get(), "/generated/ksp/$name/kotlin"))
            }
        }

        // Write built version to file after creating a bundle (needed for ci, to create the version tag)
        if (this.name.endsWith("Release")) {
            tasks.findByName("publish${this.name.capitalizeAsciiOnly()}Bundle")!!.doLast {
                File(project.layout.buildDirectory.asFile.get(), "version.tag")
                    .writeText(this@variant.versionName)
            }
        }
    }

    tasks.withType<KotlinCompilationTask<*>> {
        compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
    }
}

ksp {
    arg(
        "compose-destinations.codeGenPackageName",
        "org.datepollsystems.waiterrobot.android.generated.navigation"
    )
}

play {
    defaultToAppBundles.set(true)
    serviceAccountCredentials.set(file(".keys/service-account.json"))
    track.set("internal")
    releaseStatus.set(ReleaseStatus.COMPLETED)
}

val remoteBuild = project.findProperty("remoteBuild") == "true" // Default false
if (remoteBuild) {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/DatepollSystems/WaiterRobot-Shared-Android")
            credentials {
                username = project.property("GITHUB_PACKAGES_USERNAME") as String
                password = project.property("GITHUB_PACKAGES_PASSWORD") as String
            }
        }
    }
}

dependencies {
    if (remoteBuild) {
        implementation("${SHARED_GROUP}:shared-android:${SHARED_BASE_VERSION}.+")
    } else {
        implementation(project(":shared"))
    }

    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.appcompat)

    coreLibraryDesugaring(libs.android.desugar)

    // Compose TODO switch to BillOfMaterial (compose-bom)
    runtimeOnly(libs.androidx.compose.compiler)
    implementation(libs.androidx.compose.activity)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.core)
    implementation(libs.androidx.compose.ui.graphics)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.core)
    implementation(libs.androidx.compose.material3.core)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material.icons.extended)

    // Compose helpers
    implementation(libs.accompanist.permissions)

    // Architecture (MVI)
    implementation(libs.orbit.compose)

    // Dependency injection
    implementation(libs.koin.compose) // Not aligned with other koin version

    // SafeCompose Navigation Args
    implementation(libs.compose.destinations)
    ksp(libs.compose.destinations.ksp)

    // CameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)

    // QrCode Scanning
    implementation(libs.barcode.scanning)

    // In-App-Update support
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
}
