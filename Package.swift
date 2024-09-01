// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/DatepollSystems/WaiterRobot-Shared-Android/org/datepollsystems/waiterrobot/shared-kmmbridge/1.6.5/shared-kmmbridge-1.6.5.zip"
let remoteKotlinChecksum = "d50039ed5037ce608d00379d6ea8b7029e71371daaba74aa6dcfd5889e6f8e9f"
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