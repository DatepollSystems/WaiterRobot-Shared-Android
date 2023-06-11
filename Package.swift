// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/DatepollSystems/waiterrobot-mobile_android-shared/WaiterRobot/shared-kmmbridge/1.0.4-lava-1686511182/shared-kmmbridge-1.0.4-lava-1686511182.zip"
let remoteKotlinChecksum = "3ae8461bb2f0cc90d51fa6802bee613e91f6b5e46c761c8ddbed37aec3c0cf5b"
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