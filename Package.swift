// swift-tools-version:5.3
import PackageDescription

let remoteKotlinUrl = "https://api.github.com/repos/DatepollSystems/waiterrobot-mobile_android-shared/releases/assets/90839047.zip"
let remoteKotlinChecksum = "5275160cb0364b4a0193aee69f1be8f92e6ad34d063dac50c33ae60198713e92"
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