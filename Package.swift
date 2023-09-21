// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/DatepollSystems/waiterrobot-mobile_android-shared/WaiterRobot/shared-kmmbridge/1.0.5/shared-kmmbridge-1.0.5.zip"
let remoteKotlinChecksum = "8acd1c77e9d48174fc6df365b71cf37c5a8855c3ca6da272959e1b948c03fbe7"
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