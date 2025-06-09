package org.datepollsystems.waiterrobot.shared.features.switchevent.presentation

import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.data.objCArray
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.features.switchevent.domain.model.Event
import kotlin.native.HiddenFromObjC
import kotlin.native.ObjCName

data class SwitchEventState(
    @HiddenFromObjC
    val events: Resource<List<Event>> = Resource.Loading()
) : ViewModelState {
    @Suppress("unused") // iOS only
    @ObjCName("events")
    val eventsArray: Resource<Array<Event>> by events.objCArray()
}
