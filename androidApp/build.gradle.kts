import com.android.build.api.dsl.VariantDimension
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

private fun getVersionPropertyFile() = File(project.projectDir, "version.properties")
private val versionProperty by lazy {
    Properties().apply {
        getVersionPropertyFile().inputStream().use { load(it) }
    }
}

private val localProperties = Properties().apply {
    project.rootProject.file("local.properties")
        .takeIf { it.exists() }
        ?.inputStream()
        ?.use { load(it) }
}

fun fromProjectOrLocalProperties(name: String): Any? = run {
    project.findProperty(name) ?: localProperties.getOrDefault(name, null)
}

val SHARED_GROUP: String by project

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
        val keyPassword: String? = fromProjectOrLocalProperties("keyPassword")?.toString()
        val storePassword: String? = fromProjectOrLocalProperties("storePassword")?.toString()
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
            allowedHosts("*")
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
            allowedHosts("*")
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
            manifestPlaceholders["host"] = "my.kellner.team"
            allowedHosts("my.kellner.team")
        }
    }

    applicationVariants.all variant@{
        // Include the generated navigation sources
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir(
                    File(
                        project.layout.buildDirectory.asFile.get(),
                        "/generated/ksp/$name/kotlin"
                    )
                )
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
    releaseStatus.set(ReleaseStatus.COMPLETED)
    track.set("internal")
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.location)

    coreLibraryDesugaring(libs.android.desugar)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    runtimeOnly(libs.androidx.compose.compiler)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.core)
    implementation(libs.androidx.compose.ui.graphics)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
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

    // Stripe Tap-To-Pay
    implementation(libs.stripe.terminal)
    implementation(libs.stripe.ttp)
}

private fun VariantDimension.allowedHosts(vararg hosts: String) {
    buildConfigField(
        type = String::class.simpleName!!,
        name = "ALLOWED_HOSTS_CSV",
        value = hosts.joinToString(",", "\"", "\"")
    )
}

task("release") {
    doLast {
        val versionParam = findProperty("v")?.toString()

        val nextVersion = if (versionParam != null) {
            changeVersion(VersionNumber.fromString(versionParam))
        } else {
            VersionNumber.fromString(version.toString())
        }

        val versionTag = "android/$nextVersion"
        println("Creating git tag $versionTag")
        exec {
            commandLine("git", "tag", versionTag)
        }

        println("Push git tag $versionTag to origin")
        exec {
            commandLine("git", "push", "origin", versionTag)
        }
    }
}

task("bumpVersion") {
    doLast {
        require(System.getenv("CI")?.lowercase() == true.toString()) {
            "Only the CI is allowed to bump the version"
        }

        val currentVersion = VersionNumber.fromString(version.toString())
        val nextVersion = currentVersion.nextPatch()

        println("Bumping android version from $currentVersion to $nextVersion")
        changeVersion(nextVersion)
    }
}

private fun changeVersion(toVersion: VersionNumber): VersionNumber {
    val currentVersion = VersionNumber.fromString(version.toString())
    require(currentVersion < toVersion) {
        "The new version $toVersion must be higher than the current version $currentVersion"
    }

    val propertiesFile = getVersionPropertyFile()
    versionProperty.setProperty("androidVersion", toVersion.toString())
    propertiesFile.outputStream().use { versionProperty.store(it, null) }

    exec {
        commandLine("git", "add", propertiesFile.absolutePath)
    }
    exec {
        commandLine("git", "commit", "-m", "chore: Bump android version to $toVersion")
    }
    exec {
        commandLine("git", "push")
    }

    return toVersion
}

data class VersionNumber(val major: Int, val minor: Int, val patch: Int) {
    override fun toString(): String = "$major.$minor.$patch"

    fun nextMajor() = VersionNumber(major + 1, 0, 0)

    fun nextMinor() = VersionNumber(major, minor + 1, 0)

    fun nextPatch() = VersionNumber(major, minor, patch + 1)

    operator fun compareTo(other: VersionNumber): Int {
        return when {
            this.major != other.major -> this.major.compareTo(other.major)
            this.minor != other.minor -> this.minor.compareTo(other.minor)
            else -> this.patch.compareTo(other.patch)
        }
    }

    companion object {
        fun fromString(version: String): VersionNumber {
            val split = version.removePrefix("v").split('.')
            require(split.count() == 3) {
                "The provided version '$version' is not valid. It must follow the pattern of x.y.z (e.g. 1.2.3)"
            }

            return VersionNumber(
                major = split[0].toInt(),
                minor = split[1].toInt(),
                patch = split[2].toInt()
            )
        }
    }
}
