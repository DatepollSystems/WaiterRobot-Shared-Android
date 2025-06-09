// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/DatepollSystems/WaiterRobot-Shared-Android/org/datepollsystems/waiterrobot/shared-kmmbridge/1.7.6/shared-kmmbridge-1.7.6.zip"
let remoteKotlinChecksum = "59193fca215008f8869077441e4b586dc4ead787218b3fd67e32ff5ecdab6484"
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