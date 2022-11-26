# WaiterRobot Android & shared

This repository includes the Android App ([androidApp](./androidApp)) and the shared KMM
module ([shared](./shared)) for the WaiterRobot App. The iOS App can be
found [here](https://github.com/DatepollSystems/waiterrobot-mobile_iOS).

The Android App depends on the shared module directly through a gradle project dependency.

The shared module is also published as an SPM Package directly in this repo with a Package.swift in
the repo root. Therefore [KMMBridge](https://github.com/touchlab/KMMBridge) is used. The SPM Package
can be published by running the `KMM Bridge Publish Release` GitHub-Action (or locally
using `./gradlew kmmBridgePublish -PGITHUB_PUBLISH_TOKEN=xxx`).

# Language, libraries and tools

- [Kotlin](https://kotlinlang.org/)
- [Kotlin Multiplatform (Mobile)](https://kotlinlang.org/lp/mobile/)
- [KMMBridge](https://touchlab.github.io/KMMBridge/intro)
- [Koin](https://insert-koin.io/) Dependency injection
- [Kermit](https://github.com/touchlab/Kermit) Logger
- [Orbit MVI](https://orbit-mvi.org/) MVI implementation
- [Moko MVVM](https://github.com/icerockdev/moko-mvvm) shared viewModelScope

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
