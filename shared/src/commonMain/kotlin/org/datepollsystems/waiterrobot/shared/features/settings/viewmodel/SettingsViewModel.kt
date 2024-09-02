package org.datepollsystems.waiterrobot.shared.features.settings.viewmodel

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.order.repository.ProductRepository
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.datepollsystems.waiterrobot.shared.utils.launchCatching
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class SettingsViewModel internal constructor(
    private val tableRepo: TableRepository,
    private val productRepo: ProductRepository
) : AbstractViewModel<SettingsState, SettingsEffect>(SettingsState()) {

    override suspend fun SimpleSyntax<SettingsState, NavOrViewModelEffect<SettingsEffect>>.onCreate() {
        watchSettings()
    }

    fun refreshAll() = intent {
        coroutineScope {
            launchCatching(logger) { tableRepo.refresh() }
            launchCatching(logger) { productRepo.refresh() }
        }
    }

    fun switchEvent() = intent {
        navigator.push(Screen.SwitchEventScreen)
    }

    fun switchTheme(theme: AppTheme) {
        CommonApp.settings.theme = theme
    }

    fun initializeContactlessPayment() = intent {
        CommonApp.settings.enableContactlessPayment = true
        navigator.push(Screen.StripeInitializationScreen)
    }

    fun toggleSkipMoneyBackDialog(value: Boolean? = null, confirmed: Boolean = false) = intent {
        val newValue = value ?: !state.skipMoneyBackDialog
        if (newValue && !confirmed) {
            postSideEffect(SettingsEffect.ConfirmSkipMoneyBackDialog)
        } else {
            CommonApp.settings.skipMoneyBackDialog = newValue
        }
    }

    fun togglePaymentSelectAllProductsByDefault(value: Boolean? = null) = intent {
        CommonApp.settings.paymentSelectAllProductsByDefault =
            value ?: !state.paymentSelectAllProductsByDefault
    }

    fun logout() = intent {
        CommonApp.logout()
    }

    private fun watchSettings() = intent {
        coroutineScope {
            launch {
                CommonApp.appTheme.collect {
                    reduce { state.copy(currentAppTheme = it) }
                }
            }
            launch {
                CommonApp.settings.skipMoneyBackDialogFlow.collect {
                    reduce { state.copy(skipMoneyBackDialog = it) }
                }
            }
            launch {
                CommonApp.settings.paymentSelectAllProductsByDefaultFlow.collect {
                    reduce { state.copy(paymentSelectAllProductsByDefault = it) }
                }
            }
        }
    }
}
