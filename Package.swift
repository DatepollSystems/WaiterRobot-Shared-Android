// swift-tools-version:5.3
import PackageDescription

let remoteKotlinUrl = "https://api.github.com/repos/DatepollSystems/waiterrobot-mobile_android-shared/releases/assets/85548387.zip"
let remoteKotlinChecksum = "3c8ba49567a6c6824bc295a0f14b00499d936271107ab6d39a46bc1b12216525"
let packageName = "shared"

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