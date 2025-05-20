package org.datepollsystems.waiterrobot.shared.features.switchevent.data

import io.sentry.kotlin.multiplatform.Sentry
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.EventProvider
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.core.sentry.SentryTag
import org.datepollsystems.waiterrobot.shared.core.sentry.setTag
import org.datepollsystems.waiterrobot.shared.features.switchevent.data.remote.EventLocationApi
import org.datepollsystems.waiterrobot.shared.features.switchevent.domain.model.Event
import org.datepollsystems.waiterrobot.shared.features.switchevent.domain.repository.SwitchEventRepository
import org.datepollsystems.waiterrobot.shared.utils.extensions.runCatchingCancelable

internal class SwitchEventRepositoryImpl(
    private val eventLocationApi: EventLocationApi,
    private val eventProvider: EventProvider,
) : SwitchEventRepository, AbstractRepository() {

    override suspend fun getEvents(): Result<List<Event>> = runCatchingCancelable {
        eventLocationApi.getEvents().map {
            Event(
                id = it.id,
                name = it.name,
                startDate = it.startDate,
                endDate = it.endDate,
                city = it.city,
                organisationId = it.organisationId,
                isDemo = it.isDemo,
                stripeSettings = if (it.stripeEnabled && it.stripeLocationId != null) {
                    Event.StripeSettings.Enabled(it.stripeLocationId, it.stripeMinAmount ?: 0)
                } else {
                    Event.StripeSettings.Disabled
                }
            )
        }
    }

    override suspend fun switchToEvent(event: Event): Boolean {
        val oldEventId = eventProvider.value?.id
        CommonApp.settings.selectedEvent = event
        Sentry.configureScope { scope ->
            scope.setTag(SentryTag.EVENT_ID, event.id.toString())
        }

        if (oldEventId != event.id) {
            val stripeProvider = CommonApp.stripeProvider
            if (stripeProvider?.isInitialized() == true) stripeProvider.disconnectReader()

            // Reset ignoring card payment setup
            CommonApp.settings.enableContactlessPayment = true

            return true
        }

        return false
    }
}
