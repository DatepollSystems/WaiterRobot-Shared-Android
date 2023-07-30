// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/DatepollSystems/waiterrobot-mobile_android-shared/WaiterRobot/shared-kmmbridge/1.0.5-lava-1690737806/shared-kmmbridge-1.0.5-lava-1690737806.zip"
let remoteKotlinChecksum = "a42b381528180b814fdcd16808d090cbbe32ef77bb9ec84b6d4372c8e2f6dd4c"
let packageName = "shared"
// END KMMBRIDGE BLOCK

let package = Package(
    name: packageName,
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: packageName,
            targets: [packageName]
        ),
    ],
    targets: [
        .binaryTarget(
            name: packageName,
            url: remoteKotlinUrl,
            checksum: remoteKotlinChecksum
        )
        ,
    ]
)