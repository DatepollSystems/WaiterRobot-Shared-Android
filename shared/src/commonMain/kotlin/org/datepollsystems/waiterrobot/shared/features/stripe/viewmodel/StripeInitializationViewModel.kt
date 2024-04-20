package org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel

import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeException
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeProvider
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class StripeInitializationViewModel internal constructor(
    private val stripe: StripeProvider,
    private val permissionsController: PermissionsController
) : AbstractViewModel<StripeInitializationState, StripeInitializationEffect>(
    StripeInitializationState()
) {
    // TODO handle all the early returns
    fun enableStripe() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        val locationId =
            when (val stripeSettings = CommonApp.settings.selectedEvent?.stripeSettings) {
                null -> {
                    logger.w("Wanted to connect to local reader, but no event was selected")
                    return@intent
                }

                Event.StripeSettings.Disabled -> {
                    logger.w("Wanted to connect to local reader, but stripe is disabled for this event")
                    return@intent
                }

                is Event.StripeSettings.Enabled -> stripeSettings.locationId
            }

        if (stripe.isInitialized()) {
            logger.w("Stripe Terminal is already initialized, skipping initialization")
            return@intent
        }

        @Suppress("SwallowedException")
        try {
            permissionsController.providePermission(Permission.LOCATION)
        } catch (denied: DeniedException) {
            // TODO do we need to distinguish between denied and permanently denied?
            reduce {
                state.copy(error = StripeInitializationState.Error.GEOLOCATION_PERMISSION_DENIED)
            }
            return@intent
        }

        if (!stripe.isGeoLocationEnabled()) {
            logger.i("GPS not enabled")
            reduce {
                state.copy(error = StripeInitializationState.Error.GEOLOCATION_NOT_ENABLED)
            }
            return@intent
        }

        try {
            stripe.connectLocalReader(locationId)
        } catch (e: StripeException) {
            logger.e("Failed to connect to local reader", e)
            reduce {
                state.copy(error = StripeInitializationState.Error.READER_CONNECTION_FAILED)
            }
        }
    }
}
