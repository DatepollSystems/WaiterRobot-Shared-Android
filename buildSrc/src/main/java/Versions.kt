object Versions {
    // Android
    const val minSdk = 24
    const val targetSdk = 31
    const val androidAppVersionName = "0.1.0"

    // Generate VersionCode from VersionName (e.g. 1.2.3 -> 10203, 1.23.45 -> 12345)
    val androidAppVersionCode: Int
        get() = run {
            val parts = androidAppVersionName.split(".").map(String::toInt)
            parts[0] * 10_000 + parts[1] * 100 + parts[2]
        }

    const val buildTools = "33.0.0"
    const val compileSdk = 33

    const val compose = "1.3.0-rc01"
    const val composeCompiler = "1.3.2"
    const val material = "1.3.0-rc01"
}
