package org.datepollsystems.waiterrobot.shared.generated.localization


import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.localizedStringWithFormat

var localizationBundle = NSBundle.mainBundle()

actual fun L.App.ForceUpdate.message(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.app.forceUpdate.message", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.App.ForceUpdate.openStore(value0: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.app.forceUpdate.openStore", null, null)
    return NSString.localizedStringWithFormat(localizedString, value0 as NSString)
}

actual fun L.App.ForceUpdate.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.app.forceUpdate.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.App.name(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.app.name", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.App.UpdateAvailable.message(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.app.updateAvailable.message", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.App.UpdateAvailable.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.app.updateAvailable.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.amountToLow(value0: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.amountToLow", null, null)
    return NSString.localizedStringWithFormat(localizedString, value0 as NSString)
}

actual fun L.Billing.change(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.billing.change", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.given(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.billing.given", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.keepBill(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.billing.keepBill", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.noOpenBill(value0: String, value1: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.noOpenBill", null, null)
    return NSString.localizedStringWithFormat(
        localizedString,
        value0 as NSString,
        value1 as NSString
    )
}

actual fun L.Billing.NotSent.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.notSent.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.NotSent.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.notSent.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.pay(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.billing.pay", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.payByCard(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.payByCard", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.ProductsAlreadyPayed.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.productsAlreadyPayed.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.ProductsAlreadyPayed.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.productsAlreadyPayed.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.Stripe.Canceled.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.stripe.canceled.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.Stripe.Canceled.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.stripe.canceled.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.Stripe.Failed.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.stripe.failed.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.Stripe.Failed.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.stripe.failed.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.Stripe.locationDisabled(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.stripe.locationDisabled", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.Stripe.nfcDisabled(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.stripe.nfcDisabled", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.Stripe.success(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.billing.stripe.success", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Billing.title(value0: String, value1: String): String {
    val localizedString = localizationBundle.localizedStringForKey("l.billing.title", null, null)
    return NSString.localizedStringWithFormat(
        localizedString,
        value0 as NSString,
        value1 as NSString
    )
}

actual fun L.Billing.total(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.billing.total", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.DeepLink.alreadyLoggedIn(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.deepLink.alreadyLoggedIn", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.DeepLink.invalid(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.deepLink.invalid", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Dialog.cancel(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.dialog.cancel", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Dialog.clear(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.dialog.clear", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Dialog.closeAnyway(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.dialog.closeAnyway", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Dialog.off(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.dialog.off", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Dialog.ok(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.dialog.ok", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Dialog.on(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.dialog.on", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Dialog.save(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.dialog.save", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Exceptions.accountNotActivated(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.exceptions.accountNotActivated", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Exceptions.generic(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.exceptions.generic", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Exceptions.retry(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.exceptions.retry", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Exceptions.title(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.exceptions.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Login.DebugDialog.inputLabel(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.login.debugDialog.inputLabel", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Login.DebugDialog.placeholder(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.login.debugDialog.placeholder", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Login.desc(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.login.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Login.InvalidCode.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.login.invalidCode.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Login.InvalidCode.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.login.invalidCode.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Login.Scanner.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.login.scanner.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Login.title(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.login.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Login.withQrCode(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.login.withQrCode", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Navigation.back(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.navigation.back", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.AddNoteDialog.inputLabel(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.addNoteDialog.inputLabel", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.AddNoteDialog.inputPlaceholder(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.order.addNoteDialog.inputPlaceholder",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.AddNoteDialog.title(value0: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.addNoteDialog.title", null, null)
    return NSString.localizedStringWithFormat(localizedString, value0 as NSString)
}

actual fun L.Order.addProduct(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.order.addProduct", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.alreadyCreated(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.alreadyCreated", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.CouldNotFindProduct.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.couldNotFindProduct.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.CouldNotFindProduct.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.couldNotFindProduct.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.descAddProduct(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.descAddProduct", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.keepOrder(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.order.keepOrder", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.NotSent.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.notSent.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.NotSent.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.notSent.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.ProductSoldOut.descOrderAdd(value0: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.productSoldOut.descOrderAdd", null, null)
    return NSString.localizedStringWithFormat(localizedString, value0 as NSString)
}

actual fun L.Order.ProductSoldOut.descOrderSent(value0: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.productSoldOut.descOrderSent", null, null)
    return NSString.localizedStringWithFormat(localizedString, value0 as NSString)
}

actual fun L.Order.ProductSoldOut.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.productSoldOut.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.StockToLow.desc(value0: String, value1: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.stockToLow.desc", null, null)
    return NSString.localizedStringWithFormat(
        localizedString,
        value0 as NSString,
        value1 as NSString
    )
}

actual fun L.Order.StockToLow.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.order.stockToLow.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Order.title(value0: String, value1: String): String {
    val localizedString = localizationBundle.localizedStringForKey("l.order.title", null, null)
    return NSString.localizedStringWithFormat(
        localizedString,
        value0 as NSString,
        value1 as NSString
    )
}

actual fun L.ProductSearch.allGroups(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.productSearch.allGroups", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.ProductSearch.noProductFound(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.productSearch.noProductFound", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.ProductSearch.placeholder(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.productSearch.placeholder", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.ProductSearch.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.productSearch.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.QrScanner.cameraPermissionRequired(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.qrScanner.cameraPermissionRequired", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.QrScanner.errorOpeningCamera(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.qrScanner.errorOpeningCamera", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.QrScanner.noCameraFound(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.qrScanner.noCameraFound", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Register.alreadyRegisteredInfo(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.register.alreadyRegisteredInfo", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Register.login(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.register.login", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Register.Name.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.register.name.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Register.Name.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.register.name.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Root.InvalidLoginLink.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.root.invalidLoginLink.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Root.InvalidLoginLink.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.root.invalidLoginLink.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.About.privacyPolicy(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.about.privacyPolicy", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.About.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.about.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.About.Version.desc(value0: String, value1: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.about.version.desc", null, null)
    return NSString.localizedStringWithFormat(
        localizedString,
        value0 as NSString,
        value1 as NSString
    )
}

actual fun L.Settings.About.Version.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.about.version.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.General.DarkMode.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.general.darkMode.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.General.DarkMode.useSystem(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.settings.general.darkMode.useSystem",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.General.keepLoggedIn(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.general.keepLoggedIn", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.General.Logout.action(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.general.logout.action", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.General.Logout.desc(value0: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.general.logout.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString, value0 as NSString)
}

actual fun L.Settings.General.Logout.title(value0: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.general.logout.title", null, null)
    return NSString.localizedStringWithFormat(localizedString, value0 as NSString)
}

actual fun L.Settings.General.Refresh.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.general.refresh.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.General.Refresh.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.general.refresh.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.General.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.general.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.Payment.CardPayment.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.payment.cardPayment.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.Payment.CardPayment.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.payment.cardPayment.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.Payment.SelectAllProductsByDefault.desc(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.settings.payment.selectAllProductsByDefault.desc",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.Payment.SelectAllProductsByDefault.title(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.settings.payment.selectAllProductsByDefault.title",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.Payment.SkipMoneyBackDialog.confirmAction(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.settings.payment.skipMoneyBackDialog.confirmAction",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.Payment.SkipMoneyBackDialog.confirmDesc(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.settings.payment.skipMoneyBackDialog.confirmDesc",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.Payment.SkipMoneyBackDialog.desc(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.settings.payment.skipMoneyBackDialog.desc",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.Payment.SkipMoneyBackDialog.title(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.settings.payment.skipMoneyBackDialog.title",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.Payment.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.settings.payment.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.Settings.title(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.settings.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.continueWithoutStripe(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.continueWithoutStripe", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Error.disabled(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.error.disabled", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Error.disabledForEvent(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.error.disabledForEvent", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Error.locationPermissionDenied(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.stripeInit.error.locationPermissionDenied",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Error.readerConnectionFailed(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.stripeInit.error.readerConnectionFailed",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Error.terminalInitiationFailed(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.stripeInit.error.terminalInitiationFailed",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.locationDataSharingNotice(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.stripeInit.locationDataSharingNotice",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.EnableGeoLocation.action(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.stripeInit.step.enableGeoLocation.action",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.EnableGeoLocation.desc(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.stripeInit.step.enableGeoLocation.desc",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.EnableNfc.action(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.step.enableNfc.action", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.EnableNfc.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.step.enableNfc.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.Error.action(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.step.error.action", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.Error.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.step.error.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.Finished.action(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.step.finished.action", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.Finished.desc(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.step.finished.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.GrantLocationPermission.action(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.stripeInit.step.grantLocationPermission.action",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.GrantLocationPermission.desc(): String {
    val localizedString = localizationBundle.localizedStringForKey(
        "l.stripeInit.step.grantLocationPermission.desc",
        null,
        null
    )
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.Start.action(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.step.start.action", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.StripeInit.Step.Start.desc(value0: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.stripeInit.step.start.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString, value0 as NSString)
}

actual fun L.StripeInit.title(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.stripeInit.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.SwitchEvent.desc(): String {
    val localizedString = localizationBundle.localizedStringForKey("l.switchEvent.desc", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.SwitchEvent.noEventFound(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.switchEvent.noEventFound", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.SwitchEvent.title(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.switchEvent.title", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.TableDetail.newOrder(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.tableDetail.newOrder", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.TableDetail.noOrder(value0: String, value1: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.tableDetail.noOrder", null, null)
    return NSString.localizedStringWithFormat(
        localizedString,
        value0 as NSString,
        value1 as NSString
    )
}

actual fun L.TableDetail.title(value0: String, value1: String): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.tableDetail.title", null, null)
    return NSString.localizedStringWithFormat(
        localizedString,
        value0 as NSString,
        value1 as NSString
    )
}

actual fun L.TableList.groupFilter(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.tableList.groupFilter", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}

actual fun L.TableList.noTableFound(): String {
    val localizedString =
        localizationBundle.localizedStringForKey("l.tableList.noTableFound", null, null)
    return NSString.localizedStringWithFormat(localizedString)
}
