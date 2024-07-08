// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/DatepollSystems/WaiterRobot-Shared-Android/org/datepollsystems/waiterrobot/shared-kmmbridge/1.6.4/shared-kmmbridge-1.6.4.zip"
let remoteKotlinChecksum = "12981c1a9faef52efd5e3a920d7b1f23e2a0fef56b3ee4ad13a9d79f20cf25be"
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