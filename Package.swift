// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/DatepollSystems/waiterrobot-mobile_android-shared/WaiterRobot/shared-kmmbridge/1.0.7-lava-1696602019/shared-kmmbridge-1.0.7-lava-1696602019.zip"
let remoteKotlinChecksum = "369467976b3d4dd548595c5b8c3ff47bb32e716fc0f6b68cec9245731af39a3c"
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