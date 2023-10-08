<p style="text-align:center">
    <img alt="WaiterRobot logo" src="documentation/wr-square-rounded.png" style="width:200px; border-radius: 15px;"/><br>
    <a href="https://play.google.com/store/apps/details?id=org.datepollsystems.waiterrobot.android">
        <img alt="Get it on Google Play" height="60px" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png"/>
    </a>    
</p>
<h1 style="text-align:center">WaiterRobot</h1>
<p style="text-align:center">Lightning fast and simple gastronomy</p>

# Android & Shared

This repository includes the Android App ([androidApp](./androidApp)) and the shared KMM
module ([shared](./shared)) for the WaiterRobot App. The iOS App can be
found [here](https://github.com/DatepollSystems/waiterrobot-mobile_iOS).

The Android App depends on the shared module directly through a gradle project dependency.

The shared module is also published as an SPM Package directly in this repo with a Package.swift in
the repo root. Therefore [KMMBridge](https://github.com/touchlab/KMMBridge) is used. The SPM Package
can be published by running the `KMM Bridge Publish Release` GitHub-Action.

## iOS dev with local KMM module version

For a guide to use a local version of the KMM module
see [KMMBridge local dev spm](https://touchlab.github.io/KMMBridge/spm/IOS_LOCAL_DEV_SPM)

The main branch contains the `Package.swift` file ready for local dev.

### Short version

1. `./gradlew spmDevBuild` (must be run after each change in the KMM module)
2. Drag the whole KMM project folder (top level git folder) into the WaiterRobot project in Xcode
3. Start programming :)
4. When finished delete folder, make sure to select "Remove References"!!! (otherwise the whole KMM
   project will be deleted locally)

## Releasing

### Android

Production release is triggered on push to main. The CI then builds the app and deploys it to
the `internal` Track on Google Play. After testing the app then must be promoted to production
manually from there. A tag in the form of `android/major.minor.patch` (e.g. android/1.0.0) is
created. (see [publishAndroid.yml](.github/workflows/publishAndroid.yml))

> Do not forget to bump the android version ([version.properties](androidApp/version.properties)) on
> the dev branch after a production release was made.

On each push to develop a lava (dev) build is triggered and published to `internal` track of
the WaiterRobot Lava app on Google Play. A tag in the form
of `android/major.minor.patch-lava-epochMinutes` is created (e.g. android/1.0.1-lava-27935730). (
see [publishAndroid.yml](.github/workflows/publishAndroid.yml))

### Shared

A release is triggered on push to main or develop (only when shared module changed). The CI then
builds the shared Xcode Framework (Swift Package) and the shared Android library. Both artifacts
are uploaded to the GitHub Package registry. A tag in the form of `major.minor.patch` (
e.g. 1.0.0) is created. (see [publishShared.yml](.github/workflows/publish.yml))

> Unfortunately SPM does not support custom tag prefixes (e.g. shared/1.0.0) and kmmbridge
> does not support tag suffixes (without creating a custom release action). Therefor the currently
> the no shared identifier is added to the tag of shared releases.

# Language, libraries and tools

## Shared

- [Kotlin](https://kotlinlang.org/)
- [Kotlin Multiplatform (Mobile)](https://kotlinlang.org/lp/mobile/)
- [KMMBridge](https://kmmbridge.touchlab.co/docs/)
- [Ktor](https://ktor.io/) Http client
    - [Content Negotiation](https://ktor.io/docs/serialization-client.html) Body serialization
    - [Client Auth](https://ktor.io/docs/auth.html) Authentication (Bearer)
    - [Client Logging](https://ktor.io/docs/client-logging.html) Logging
- [Realm (Kotlin)](https://github.com/realm/realm-kotlin) Database
- [Koin](https://insert-koin.io/) Dependency injection
- [Kermit](https://github.com/touchlab/Kermit) Logger
- [Orbit MVI](https://orbit-mvi.org/) MVI implementation
- [Moko MVVM](https://github.com/icerockdev/moko-mvvm) shared viewModelScope
- [KMM Resources](https://github.com/jcraane/kmm-resources) shared localization
- [KotlinX DateTime](https://github.com/Kotlin/kotlinx-datetime) Multiplatform DateTime
- [KotlinX Serialization (Json)](https://github.com/Kotlin/kotlinx.serialization) JSON serialization
- [BuildKonfig](https://github.com/yshrsmz/BuildKonfig) BuildConfig for Kotlin Multiplatform Projects

## Android

- [Jetpack Compose](https://developer.android.com/jetpack/compose) Declarative UI
    - [Material](https://developer.android.com/jetpack/androidx/releases/compose-material) Material
      UI
    - [Material Icons](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary)
      Material UI Icons (extended)
    - [Compose Destinations](https://composedestinations.rafaelcosta.xyz/) Typesafe navigation for
      Jetpack Compose
    - [Accompanist Permissions](https://google.github.io/accompanist/permissions/) Helper for
      permission management in JetpackCompose
- [Barcode-Scanning](https://developers.google.com/ml-kit/vision/barcode-scanning/android)
  QR/Barcode scanner
- [Gradle Play Publisher](https://github.com/Triple-T/gradle-play-publisher)

# Further Resources and References

- [Kotlin/kmm-sample](https://github.com/Kotlin/kmm-sample)
- [KaMPKit](https://github.com/touchlab/KaMPKit) Collection of code and tools for getting started
  with KMP/KMM
- [KMMBridge sample](https://github.com/touchlab/KMMBridgeSKIETemplate)
- [joreilly/PeopleInSpace](https://github.com/joreilly/PeopleInSpace) Minimal KMP project using
  SwiftUI, Jetpack Compose, SQLDelight, Koin (also includes many other platforms)
- [KMP library/tool collection 1](https://github.com/AAkira/Kotlin-Multiplatform-Libraries)
- [KMP library/tool collection 2](https://github.com/terrakok/kmm-awesome)
- [Koin for KMP](https://insert-koin.io/docs/reference/koin-mp/kmp)
- Converting Kotlin Flow to Swift Combine Publisher
    - [John O'Reilly - Wrapping Kotlin Flow with Swift Combine Publisher in a Kotlin Multiplatform project](https://johnoreilly.dev/posts/kotlinmultiplatform-swift-combine_publisher-flow)
    - [Guilherme Delgado - Kotlin Multiplatform Mobile — sharing the UI State management](https://proandroiddev.com/kotlin-multiplatform-mobile-sharing-the-ui-state-management-a67bd9a49882)
    - [Orbit MVI - Swift Gradle Plugin](https://github.com/orbit-mvi/orbit-swift-gradle-plugin/blob/main/src/main/resources/Publisher.swift.mustache)
