# WaiterRobot Android & shared

This repository includes the Android App ([androidApp](./androidApp)) and the shared KMM
module ([shared](./shared)) for the WaiterRobot App. The iOS App can be
found [here](https://github.com/DatepollSystems/waiterrobot-mobile_iOS).

The Android App depends on the shared module directly through a gradle project dependency.

The shared module is also published as an SPM Package directly in this repo with a Package.swift in
the repo root. Therefore [KMMBridge](https://github.com/touchlab/KMMBridge) is used. The SPM Package
can be published by running `./gradlew kmmBridgePublish -PGITHUB_PUBLISH_TOKEN=xxx`.