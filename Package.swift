// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/DatepollSystems/waiterrobot-mobile_android-shared/WaiterRobot/shared-kmmbridge/1.0.5-lava-1692283877/shared-kmmbridge-1.0.5-lava-1692283877.zip"
let remoteKotlinChecksum = "5fe05b8c268e2778988ce34cb1b34628b4e1b3f774cf465cdf7c58d2fa5616e2"
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