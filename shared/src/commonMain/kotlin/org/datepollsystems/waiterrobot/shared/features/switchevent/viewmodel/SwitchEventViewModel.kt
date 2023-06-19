package org.datepollsystems.waiterrobot.shared.features.switchevent.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event
import org.datepollsystems.waiterrobot.shared.features.switchevent.repository.SwitchEventRepository
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class SwitchEventViewModel internal constructor(
    private val repository: SwitchEventRepository,
) : AbstractViewModel<SwitchEventState, SwitchEventEffect>(SwitchEventState()) {

    override fun onCreate(state: SwitchEventState) {
        loadEvents()
    }

    fun loadEvents() = intent {
        reduce { state.copy(viewState = ViewState.Loading) }

        val events = repository.getEvents()

        reduce { state.copy(viewState = ViewState.Idle, events = events) }
    }

    fun onEventSelected(event: Event) = intent {
        repository.switchToEvent(event)

        updateParent<TableListViewModel>()

        navigator.popUpToRoot()
    }
}
