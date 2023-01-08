# WaiterRobot Android & shared

This repository includes the Android App ([androidApp](./androidApp)) and the shared KMM
module ([shared](./shared)) for the WaiterRobot App. The iOS App can be
found [here](https://github.com/DatepollSystems/waiterrobot-mobile_iOS).

The Android App depends on the shared module directly through a gradle project dependency.

The shared module is also published as an SPM Package directly in this repo with a Package.swift in
the repo root. Therefore [KMMBridge](https://github.com/touchlab/KMMBridge) is used. The SPM Package
can be published by running the `KMM Bridge Publish Release` GitHub-Action.

If you want to build and publish the SPM locally you can do so with the
command `./gradlew kmmBridgePublish -PGITHUB_PUBLISH_TOKEN=xxx` (personal github token is needed and
only possible on OSX). Before running create a new branch from the remote main branch (make sure to
commit or stash all local changes before) and push the new branch to remote. After that run the
command. When finished you delete the temporary created branch. This is done because while
publishing the Package.swift file is newly generated and committed to the repository. This can lead
to conflicts and is constantly changing the `Package.swift` file for local dev and releasing. (
see [here](https://touchlab.github.io/KMMBridge/ciconfig/GITHUB_ACTIONS#workflow-options))

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

# Language, libraries and tools

## Shared

- [Kotlin](https://kotlinlang.org/)
- [Kotlin Multiplatform (Mobile)](https://kotlinlang.org/lp/mobile/)
- [KMMBridge](https://touchlab.github.io/KMMBridge/intro)
- [Ktor](https://ktor.io/) Http client
    - [Content Negotiation](https://ktor.io/docs/serialization-client.html) Body serialization
    - [Client Auth](https://ktor.io/docs/auth.html) Authentication (Bearer)
    - [Client Logging](https://ktor.io/docs/client-logging.html) Logging
- [Koin](https://insert-koin.io/) Dependency injection
- [Kermit](https://github.com/touchlab/Kermit) Logger
- [Orbit MVI](https://orbit-mvi.org/) MVI implementation
- [Moko MVVM](https://github.com/icerockdev/moko-mvvm) shared viewModelScope
- [KMM Resources](https://github.com/jcraane/kmm-resources) shared localization
- [KotlinX DateTime](https://github.com/Kotlin/kotlinx-datetime) Multiplatform DateTime
- [KotlinX Serialization (Json)](https://github.com/Kotlin/kotlinx.serialization) JSON serialization

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

# Further Resources and References

- [Kotlin/kmm-sample](https://github.com/Kotlin/kmm-sample)
- [KaMPKit](https://github.com/touchlab/KaMPKit) Collection of code and tools for getting stated
  with KMP/KMM
- [KMMBridge SPM sample (android + shared)](https://github.com/touchlab/KMMBridgeSampleKotlin)
- [KMMBridge SPM sample (iOS)](https://github.com/touchlab/KMMBridgeSampleSpm)
- [joreilly/PeopleInSpace](https://github.com/joreilly/PeopleInSpace) Minimal KMP project using
  SwiftUI, Jetpack Compose, SQLDelight, Koin (also includes many other platforms)
- [KMP library/tool collection 1](https://github.com/AAkira/Kotlin-Multiplatform-Libraries)
- [KMP library/tool collection 2](https://github.com/terrakok/kmm-awesome)
- [Koin for KMP](https://insert-koin.io/docs/reference/koin-mp/kmp)
