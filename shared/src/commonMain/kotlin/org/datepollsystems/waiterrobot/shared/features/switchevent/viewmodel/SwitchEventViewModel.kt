package org.datepollsystems.waiterrobot.shared.features.switchevent.viewmodel

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event
import org.datepollsystems.waiterrobot.shared.features.switchevent.repository.SwitchEventRepository
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class SwitchEventViewModel internal constructor(
    private val repository: SwitchEventRepository,
) : AbstractViewModel<SwitchEventState, SwitchEventEffect>(SwitchEventState()) {

    override suspend fun SimpleSyntax<SwitchEventState, NavOrViewModelEffect<SwitchEventEffect>>.onCreate() {
        loadEvents()
    }

    fun loadEvents() = intent {
        reduce { state.copy(viewState = ViewState.Loading) }

        val events = repository.getEvents()

        reduce { state.copy(viewState = ViewState.Idle, events = events) }
    }

    fun onEventSelected(event: Event) = intent {
        val switchedEvent = repository.switchToEvent(event)

        updateParent<TableListViewModel>()

        if (switchedEvent && CommonApp.settings.selectedEvent?.stripeSettings is Event.StripeSettings.Enabled) {
            navigator.push(Screen.StripeInitializationScreen)
        } else {
            navigator.popUpToRoot()
        }
    }
}
