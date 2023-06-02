package org.datepollsystems.waiterrobot.shared.features.settings.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.order.repository.ProductRepository
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class SettingsViewModel internal constructor(
    private val tableRepo: TableRepository,
    private val productRepo: ProductRepository
) : AbstractViewModel<SettingsState, SettingsEffect>(SettingsState()) {

    override fun onCreate(state: SettingsState) {
        watchAppTheme()
    }

    fun refreshAll() = intent {
        coroutineScope {
            listOf(
                launchCatching { tableRepo.getTables(true) },
                launchCatching { productRepo.getProducts(true) }
            ).joinAll()
        }
        updateParent<TableListViewModel>()
    }

    fun switchEvent() = intent {
        navigator.push(Screen.SwitchEventScreen)
    }

    fun switchTheme(theme: AppTheme) {
        CommonApp.settings.appTheme = theme
    }

    fun logout() = intent {
        CommonApp.logout()
    }

    private fun CoroutineScope.launchCatching(block: suspend () -> Unit) = launch {
        runCatching {
            block()
        }
    }

    private fun watchAppTheme() = intent {
        CommonApp.appTheme.collect {
            reduce { state.copy(currentAppTheme = it) }
        }
    }
}
