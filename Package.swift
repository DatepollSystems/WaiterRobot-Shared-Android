// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/DatepollSystems/WaiterRobot-Shared-Android/org/datepollsystems/waiterrobot/shared-kmmbridge/1.5.11/shared-kmmbridge-1.5.11.zip"
let remoteKotlinChecksum = "31082c2c43bfe8884957919c9c9be5aa3bfc33ebe72410219ad0bfd59a16b192"
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