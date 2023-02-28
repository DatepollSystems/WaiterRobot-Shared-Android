// swift-tools-version:5.3
import PackageDescription

let remoteKotlinUrl = "https://api.github.com/repos/DatepollSystems/waiterrobot-mobile_android-shared/releases/assets/97431754.zip"
let remoteKotlinChecksum = "3bc8a07a2b09cc55545cef5defc252cec85bcb96a73020059211deea837c8891"
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