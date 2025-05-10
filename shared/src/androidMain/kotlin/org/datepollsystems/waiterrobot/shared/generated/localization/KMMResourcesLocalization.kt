package org.datepollsystems.waiterrobot.shared.generated.localization

import android.content.Context
import org.datepollsystems.waiterrobot.shared.R

var localizationContext: Context? = null

actual fun L.App.ForceUpdate.message(): String =
    localizationContext?.getString(R.string.l_app_forceUpdate_message) ?: ""

actual fun L.App.ForceUpdate.openStore(value0: String): String =
    localizationContext?.getString(R.string.l_app_forceUpdate_openStore, value0) ?: ""

actual fun L.App.ForceUpdate.title(): String =
    localizationContext?.getString(R.string.l_app_forceUpdate_title) ?: ""

actual fun L.App.name(): String = localizationContext?.getString(R.string.l_app_name) ?: ""
actual fun L.App.UpdateAvailable.message(): String =
    localizationContext?.getString(R.string.l_app_updateAvailable_message) ?: ""

actual fun L.App.UpdateAvailable.title(): String =
    localizationContext?.getString(R.string.l_app_updateAvailable_title) ?: ""

actual fun L.Billing.amountToLow(value0: String): String =
    localizationContext?.getString(R.string.l_billing_amountToLow, value0) ?: ""

actual fun L.Billing.change(): String =
    localizationContext?.getString(R.string.l_billing_change) ?: ""

actual fun L.Billing.given(): String =
    localizationContext?.getString(R.string.l_billing_given) ?: ""

actual fun L.Billing.keepBill(): String =
    localizationContext?.getString(R.string.l_billing_keepBill) ?: ""

actual fun L.Billing.noOpenBill(value0: String, value1: String): String =
    localizationContext?.getString(R.string.l_billing_noOpenBill, value0, value1) ?: ""

actual fun L.Billing.NotSent.desc(): String =
    localizationContext?.getString(R.string.l_billing_notSent_desc) ?: ""

actual fun L.Billing.NotSent.title(): String =
    localizationContext?.getString(R.string.l_billing_notSent_title) ?: ""

actual fun L.Billing.pay(): String = localizationContext?.getString(R.string.l_billing_pay) ?: ""
actual fun L.Billing.payByCard(): String =
    localizationContext?.getString(R.string.l_billing_payByCard) ?: ""

actual fun L.Billing.ProductsAlreadyPayed.desc(): String =
    localizationContext?.getString(R.string.l_billing_productsAlreadyPayed_desc) ?: ""

actual fun L.Billing.ProductsAlreadyPayed.title(): String =
    localizationContext?.getString(R.string.l_billing_productsAlreadyPayed_title) ?: ""

actual fun L.Billing.Stripe.Canceled.desc(): String =
    localizationContext?.getString(R.string.l_billing_stripe_canceled_desc) ?: ""

actual fun L.Billing.Stripe.Canceled.title(): String =
    localizationContext?.getString(R.string.l_billing_stripe_canceled_title) ?: ""

actual fun L.Billing.Stripe.Failed.desc(): String =
    localizationContext?.getString(R.string.l_billing_stripe_failed_desc) ?: ""

actual fun L.Billing.Stripe.Failed.title(): String =
    localizationContext?.getString(R.string.l_billing_stripe_failed_title) ?: ""

actual fun L.Billing.Stripe.locationDisabled(): String =
    localizationContext?.getString(R.string.l_billing_stripe_locationDisabled) ?: ""

actual fun L.Billing.Stripe.nfcDisabled(): String =
    localizationContext?.getString(R.string.l_billing_stripe_nfcDisabled) ?: ""

actual fun L.Billing.Stripe.success(): String =
    localizationContext?.getString(R.string.l_billing_stripe_success) ?: ""

actual fun L.Billing.title(value0: String, value1: String): String =
    localizationContext?.getString(R.string.l_billing_title, value0, value1) ?: ""

actual fun L.Billing.total(): String =
    localizationContext?.getString(R.string.l_billing_total) ?: ""

actual fun L.DeepLink.alreadyLoggedIn(): String =
    localizationContext?.getString(R.string.l_deepLink_alreadyLoggedIn) ?: ""

actual fun L.DeepLink.invalid(): String =
    localizationContext?.getString(R.string.l_deepLink_invalid) ?: ""

actual fun L.Dialog.cancel(): String =
    localizationContext?.getString(R.string.l_dialog_cancel) ?: ""

actual fun L.Dialog.clear(): String = localizationContext?.getString(R.string.l_dialog_clear) ?: ""
actual fun L.Dialog.closeAnyway(): String =
    localizationContext?.getString(R.string.l_dialog_closeAnyway) ?: ""

actual fun L.Dialog.off(): String = localizationContext?.getString(R.string.l_dialog_off) ?: ""
actual fun L.Dialog.ok(): String = localizationContext?.getString(R.string.l_dialog_ok) ?: ""
actual fun L.Dialog.on(): String = localizationContext?.getString(R.string.l_dialog_on) ?: ""
actual fun L.Dialog.save(): String = localizationContext?.getString(R.string.l_dialog_save) ?: ""
actual fun L.Exceptions.accountNotActivated(): String =
    localizationContext?.getString(R.string.l_exceptions_accountNotActivated) ?: ""

actual fun L.Exceptions.generic(): String =
    localizationContext?.getString(R.string.l_exceptions_generic) ?: ""

actual fun L.Exceptions.retry(): String =
    localizationContext?.getString(R.string.l_exceptions_retry) ?: ""

actual fun L.Exceptions.title(): String =
    localizationContext?.getString(R.string.l_exceptions_title) ?: ""

actual fun L.Login.DebugDialog.inputLabel(): String =
    localizationContext?.getString(R.string.l_login_debugDialog_inputLabel) ?: ""

actual fun L.Login.DebugDialog.placeholder(): String =
    localizationContext?.getString(R.string.l_login_debugDialog_placeholder) ?: ""

actual fun L.Login.desc(): String = localizationContext?.getString(R.string.l_login_desc) ?: ""
actual fun L.Login.InvalidCode.desc(): String =
    localizationContext?.getString(R.string.l_login_invalidCode_desc) ?: ""

actual fun L.Login.InvalidCode.title(): String =
    localizationContext?.getString(R.string.l_login_invalidCode_title) ?: ""

actual fun L.Login.Scanner.desc(): String =
    localizationContext?.getString(R.string.l_login_scanner_desc) ?: ""

actual fun L.Login.title(): String = localizationContext?.getString(R.string.l_login_title) ?: ""
actual fun L.Login.withQrCode(): String =
    localizationContext?.getString(R.string.l_login_withQrCode) ?: ""

actual fun L.Navigation.back(): String =
    localizationContext?.getString(R.string.l_navigation_back) ?: ""

actual fun L.Order.AddNoteDialog.inputLabel(): String =
    localizationContext?.getString(R.string.l_order_addNoteDialog_inputLabel) ?: ""

actual fun L.Order.AddNoteDialog.inputPlaceholder(): String =
    localizationContext?.getString(R.string.l_order_addNoteDialog_inputPlaceholder) ?: ""

actual fun L.Order.AddNoteDialog.title(value0: String): String =
    localizationContext?.getString(R.string.l_order_addNoteDialog_title, value0) ?: ""

actual fun L.Order.addProduct(): String =
    localizationContext?.getString(R.string.l_order_addProduct) ?: ""

actual fun L.Order.alreadyCreated(): String =
    localizationContext?.getString(R.string.l_order_alreadyCreated) ?: ""

actual fun L.Order.CouldNotFindProduct.desc(): String =
    localizationContext?.getString(R.string.l_order_couldNotFindProduct_desc) ?: ""

actual fun L.Order.CouldNotFindProduct.title(): String =
    localizationContext?.getString(R.string.l_order_couldNotFindProduct_title) ?: ""

actual fun L.Order.descAddProduct(): String =
    localizationContext?.getString(R.string.l_order_descAddProduct) ?: ""

actual fun L.Order.keepOrder(): String =
    localizationContext?.getString(R.string.l_order_keepOrder) ?: ""

actual fun L.Order.NotSent.desc(): String =
    localizationContext?.getString(R.string.l_order_notSent_desc) ?: ""

actual fun L.Order.NotSent.title(): String =
    localizationContext?.getString(R.string.l_order_notSent_title) ?: ""

actual fun L.Order.ProductSoldOut.descOrderAdd(value0: String): String =
    localizationContext?.getString(R.string.l_order_productSoldOut_descOrderAdd, value0) ?: ""

actual fun L.Order.ProductSoldOut.descOrderSent(value0: String): String =
    localizationContext?.getString(R.string.l_order_productSoldOut_descOrderSent, value0) ?: ""

actual fun L.Order.ProductSoldOut.title(): String =
    localizationContext?.getString(R.string.l_order_productSoldOut_title) ?: ""

actual fun L.Order.StockToLow.desc(value0: String, value1: String): String =
    localizationContext?.getString(R.string.l_order_stockToLow_desc, value0, value1) ?: ""

actual fun L.Order.StockToLow.title(): String =
    localizationContext?.getString(R.string.l_order_stockToLow_title) ?: ""

actual fun L.Order.title(value0: String, value1: String): String =
    localizationContext?.getString(R.string.l_order_title, value0, value1) ?: ""

actual fun L.ProductSearch.allGroups(): String =
    localizationContext?.getString(R.string.l_productSearch_allGroups) ?: ""

actual fun L.ProductSearch.noProductFound(): String =
    localizationContext?.getString(R.string.l_productSearch_noProductFound) ?: ""

actual fun L.ProductSearch.placeholder(): String =
    localizationContext?.getString(R.string.l_productSearch_placeholder) ?: ""

actual fun L.ProductSearch.title(): String =
    localizationContext?.getString(R.string.l_productSearch_title) ?: ""

actual fun L.QrScanner.cameraPermissionRequired(): String =
    localizationContext?.getString(R.string.l_qrScanner_cameraPermissionRequired) ?: ""

actual fun L.QrScanner.errorOpeningCamera(): String =
    localizationContext?.getString(R.string.l_qrScanner_errorOpeningCamera) ?: ""

actual fun L.QrScanner.noCameraFound(): String =
    localizationContext?.getString(R.string.l_qrScanner_noCameraFound) ?: ""

actual fun L.Register.alreadyRegisteredInfo(): String =
    localizationContext?.getString(R.string.l_register_alreadyRegisteredInfo) ?: ""

actual fun L.Register.login(): String =
    localizationContext?.getString(R.string.l_register_login) ?: ""

actual fun L.Register.Name.desc(): String =
    localizationContext?.getString(R.string.l_register_name_desc) ?: ""

actual fun L.Register.Name.title(): String =
    localizationContext?.getString(R.string.l_register_name_title) ?: ""

actual fun L.Root.InvalidLoginLink.desc(): String =
    localizationContext?.getString(R.string.l_root_invalidLoginLink_desc) ?: ""

actual fun L.Root.InvalidLoginLink.title(): String =
    localizationContext?.getString(R.string.l_root_invalidLoginLink_title) ?: ""

actual fun L.Settings.About.privacyPolicy(): String =
    localizationContext?.getString(R.string.l_settings_about_privacyPolicy) ?: ""

actual fun L.Settings.About.title(): String =
    localizationContext?.getString(R.string.l_settings_about_title) ?: ""

actual fun L.Settings.About.Version.desc(value0: String, value1: String): String =
    localizationContext?.getString(R.string.l_settings_about_version_desc, value0, value1) ?: ""

actual fun L.Settings.About.Version.title(): String =
    localizationContext?.getString(R.string.l_settings_about_version_title) ?: ""

actual fun L.Settings.General.DarkMode.title(): String =
    localizationContext?.getString(R.string.l_settings_general_darkMode_title) ?: ""

actual fun L.Settings.General.DarkMode.useSystem(): String =
    localizationContext?.getString(R.string.l_settings_general_darkMode_useSystem) ?: ""

actual fun L.Settings.General.keepLoggedIn(): String =
    localizationContext?.getString(R.string.l_settings_general_keepLoggedIn) ?: ""

actual fun L.Settings.General.Logout.action(): String =
    localizationContext?.getString(R.string.l_settings_general_logout_action) ?: ""

actual fun L.Settings.General.Logout.desc(value0: String): String =
    localizationContext?.getString(R.string.l_settings_general_logout_desc, value0) ?: ""

actual fun L.Settings.General.Logout.title(value0: String): String =
    localizationContext?.getString(R.string.l_settings_general_logout_title, value0) ?: ""

actual fun L.Settings.General.Refresh.desc(): String =
    localizationContext?.getString(R.string.l_settings_general_refresh_desc) ?: ""

actual fun L.Settings.General.Refresh.title(): String =
    localizationContext?.getString(R.string.l_settings_general_refresh_title) ?: ""

actual fun L.Settings.General.title(): String =
    localizationContext?.getString(R.string.l_settings_general_title) ?: ""

actual fun L.Settings.Payment.CardPayment.desc(): String =
    localizationContext?.getString(R.string.l_settings_payment_cardPayment_desc) ?: ""

actual fun L.Settings.Payment.CardPayment.title(): String =
    localizationContext?.getString(R.string.l_settings_payment_cardPayment_title) ?: ""

actual fun L.Settings.Payment.SelectAllProductsByDefault.desc(): String =
    localizationContext?.getString(R.string.l_settings_payment_selectAllProductsByDefault_desc)
        ?: ""

actual fun L.Settings.Payment.SelectAllProductsByDefault.title(): String =
    localizationContext?.getString(R.string.l_settings_payment_selectAllProductsByDefault_title)
        ?: ""

actual fun L.Settings.Payment.SkipMoneyBackDialog.confirmAction(): String =
    localizationContext?.getString(R.string.l_settings_payment_skipMoneyBackDialog_confirmAction)
        ?: ""

actual fun L.Settings.Payment.SkipMoneyBackDialog.confirmDesc(): String =
    localizationContext?.getString(R.string.l_settings_payment_skipMoneyBackDialog_confirmDesc)
        ?: ""

actual fun L.Settings.Payment.SkipMoneyBackDialog.desc(): String =
    localizationContext?.getString(R.string.l_settings_payment_skipMoneyBackDialog_desc) ?: ""

actual fun L.Settings.Payment.SkipMoneyBackDialog.title(): String =
    localizationContext?.getString(R.string.l_settings_payment_skipMoneyBackDialog_title) ?: ""

actual fun L.Settings.Payment.title(): String =
    localizationContext?.getString(R.string.l_settings_payment_title) ?: ""

actual fun L.Settings.title(): String =
    localizationContext?.getString(R.string.l_settings_title) ?: ""

actual fun L.StripeInit.continueWithoutStripe(): String =
    localizationContext?.getString(R.string.l_stripeInit_continueWithoutStripe) ?: ""

actual fun L.StripeInit.Error.disabled(): String =
    localizationContext?.getString(R.string.l_stripeInit_error_disabled) ?: ""

actual fun L.StripeInit.Error.disabledForEvent(): String =
    localizationContext?.getString(R.string.l_stripeInit_error_disabledForEvent) ?: ""

actual fun L.StripeInit.Error.locationPermissionDenied(): String =
    localizationContext?.getString(R.string.l_stripeInit_error_locationPermissionDenied) ?: ""

actual fun L.StripeInit.Error.readerConnectionFailed(): String =
    localizationContext?.getString(R.string.l_stripeInit_error_readerConnectionFailed) ?: ""

actual fun L.StripeInit.Error.terminalInitiationFailed(): String =
    localizationContext?.getString(R.string.l_stripeInit_error_terminalInitiationFailed) ?: ""

actual fun L.StripeInit.locationDataSharingNotice(): String =
    localizationContext?.getString(R.string.l_stripeInit_locationDataSharingNotice) ?: ""

actual fun L.StripeInit.Step.EnableGeoLocation.action(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_enableGeoLocation_action) ?: ""

actual fun L.StripeInit.Step.EnableGeoLocation.desc(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_enableGeoLocation_desc) ?: ""

actual fun L.StripeInit.Step.EnableNfc.action(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_enableNfc_action) ?: ""

actual fun L.StripeInit.Step.EnableNfc.desc(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_enableNfc_desc) ?: ""

actual fun L.StripeInit.Step.Error.action(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_error_action) ?: ""

actual fun L.StripeInit.Step.Error.desc(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_error_desc) ?: ""

actual fun L.StripeInit.Step.Finished.action(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_finished_action) ?: ""

actual fun L.StripeInit.Step.Finished.desc(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_finished_desc) ?: ""

actual fun L.StripeInit.Step.GrantLocationPermission.action(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_grantLocationPermission_action) ?: ""

actual fun L.StripeInit.Step.GrantLocationPermission.desc(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_grantLocationPermission_desc) ?: ""

actual fun L.StripeInit.Step.Start.action(): String =
    localizationContext?.getString(R.string.l_stripeInit_step_start_action) ?: ""

actual fun L.StripeInit.Step.Start.desc(value0: String): String =
    localizationContext?.getString(R.string.l_stripeInit_step_start_desc, value0) ?: ""

actual fun L.StripeInit.title(): String =
    localizationContext?.getString(R.string.l_stripeInit_title) ?: ""

actual fun L.SwitchEvent.desc(): String =
    localizationContext?.getString(R.string.l_switchEvent_desc) ?: ""

actual fun L.SwitchEvent.noEventFound(): String =
    localizationContext?.getString(R.string.l_switchEvent_noEventFound) ?: ""

actual fun L.SwitchEvent.title(): String =
    localizationContext?.getString(R.string.l_switchEvent_title) ?: ""

actual fun L.TableDetail.newOrder(): String =
    localizationContext?.getString(R.string.l_tableDetail_newOrder) ?: ""

actual fun L.TableDetail.noOrder(value0: String, value1: String): String =
    localizationContext?.getString(R.string.l_tableDetail_noOrder, value0, value1) ?: ""

actual fun L.TableDetail.title(value0: String, value1: String): String =
    localizationContext?.getString(R.string.l_tableDetail_title, value0, value1) ?: ""

actual fun L.TableList.groupFilter(): String =
    localizationContext?.getString(R.string.l_tableList_groupFilter) ?: ""

actual fun L.TableList.noTableFound(): String =
    localizationContext?.getString(R.string.l_tableList_noTableFound) ?: ""
