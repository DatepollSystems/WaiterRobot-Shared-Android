package org.datepollsystems.waiterrobot.shared.features.settings.viewmodel

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.order.repository.ProductRepository
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.datepollsystems.waiterrobot.shared.utils.launchCatching
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class SettingsViewModel internal constructor(
    private val tableRepo: TableRepository,
    private val productRepo: ProductRepository
) : AbstractViewModel<SettingsState, SettingsEffect>(SettingsState()) {

    override suspend fun SimpleSyntax<SettingsState, NavOrViewModelEffect<SettingsEffect>>.onCreate() {
        watchAppTheme()
    }

    fun refreshAll() = intent {
        coroutineScope {
            listOf(
                launchCatching(logger) { tableRepo.refresh() },
                launchCatching(logger) { productRepo.refresh() }
            ).joinAll()
        }
        updateParent<TableListViewModel>()
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

    fun logout() = intent {
        CommonApp.logout()
    }

    private fun watchAppTheme() = intent {
        CommonApp.appTheme.collect {
            reduce { state.copy(currentAppTheme = it) }
        }
    }
}
