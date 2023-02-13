// swift-tools-version:5.3
import PackageDescription

let remoteKotlinUrl = "https://api.github.com/repos/DatepollSystems/waiterrobot-mobile_android-shared/releases/assets/95492986.zip"
let remoteKotlinChecksum = "a36eeb57f57fd094f514197f4077c5405792dd8862e63f38ea646a5935127231"
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