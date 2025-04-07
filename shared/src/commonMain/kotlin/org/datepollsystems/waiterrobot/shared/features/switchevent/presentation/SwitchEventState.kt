package org.datepollsystems.waiterrobot.shared.features.switchevent.presentation

import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.features.switchevent.domain.model.Event

data class SwitchEventState(
    val events: Resource<List<Event>> = Resource.Loading()
) : ViewModelState
