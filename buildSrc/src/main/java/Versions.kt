object Versions {
    // Shared
    const val kermitLogger = "1.2.2"
    const val koinDi = "3.3.3"
    const val orbitMvi = "4.5.0"
    const val mokoMvvm = "0.15.0"
    const val ktor = "2.2.3"
    const val settings = "1.0.0"
    const val realm = "1.6.1" // Also update belonging plugin in shared/build.gradle.kts

    // Android
    const val androidMinSdk = 24
    const val androidTargetSdk = 31
    const val androidCompileSdk = 33
    const val androidBuildTools = "33.0.0"
    const val androidAppVersionName = "0.2.1"

    // Generate VersionCode from VersionName (e.g. 1.2.3 -> 10203, 1.23.45 -> 12345)
    val androidAppVersionCode: Int
        get() {
            val (major, minor, patch) = androidAppVersionName.split(".").map(String::toInt)
            return major * 10_000 + minor * 100 + patch
        }

    const val compose = "1.3.1"
    const val composeCompiler = "1.4.2"
    const val androidxLifecycle = "2.5.1"
    const val composeDestinations = "1.8.33-beta"
    const val camera = "1.2.1"
}
