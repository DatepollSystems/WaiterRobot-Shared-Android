object Versions {
    // Shared
    const val kermitLogger = "1.2.2"
    const val koinDi = "3.2.2"
    const val orbitMvi = "4.4.0"
    const val mokoMvvm = "0.14.0"
    const val ktor = "2.2.1"
    const val settings = "1.0.0-RC"
    const val realm = "1.5.2"

    // Android
    const val androidMinSdk = 24
    const val androidTargetSdk = 31
    const val androidCompileSdk = 33
    const val androidBuildTools = "33.0.0"
    const val androidAppVersionName = "0.2.1"

    // Generate VersionCode from VersionName (e.g. 1.2.3 -> 10203, 1.23.45 -> 12345)
    val androidAppVersionCode: Int
        get() = run {
            val parts = androidAppVersionName.split(".").map(String::toInt)
            parts[0] * 10_000 + parts[1] * 100 + parts[2]
        }

    const val compose = "1.3.1"
    const val composeCompiler = "1.3.2"
    const val androidxLifecycle = "2.5.1"
    const val composeDestinations = "1.7.27-beta"
    const val camera = "1.2.0-beta02"
}
