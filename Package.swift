// swift-tools-version:5.3
import PackageDescription

let remoteKotlinUrl = "https://api.github.com/repos/DatepollSystems/waiterrobot-mobile_android-shared/releases/assets/85596890.zip"
let remoteKotlinChecksum = "26130f106262ee5b246791ec4d41410ef3779900bf3ae1ec9703cc8bf73abc7e"
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