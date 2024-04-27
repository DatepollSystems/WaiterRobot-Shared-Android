package org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel

import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeException
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeProvider
import org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel.StripeInitializationState.State
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class StripeInitializationViewModel internal constructor(
    private val stripe: StripeProvider,
    val permissionsController: PermissionsController
) : AbstractViewModel<StripeInitializationState, StripeInitializationEffect>(
    StripeInitializationState()
) {
    // TODO handle all the early returns
    fun enableStripe() = intent {
        reduce { state.copy(initializationState = State.Started) }

        val locationId =
            when (val stripeSettings = CommonApp.settings.selectedEvent?.stripeSettings) {
                null -> {
                    logger.w("Wanted to connect to local reader, but no event was selected")
                    reduce { state.copy(initializationState = State.Error.NoEventSelected) }
                    return@intent
                }

                Event.StripeSettings.Disabled -> {
                    logger.w("Wanted to connect to local reader, but stripe is disabled for this event")
                    reduce { state.copy(initializationState = State.Error.StripeDisabledForEvent) }
                    return@intent
                }

                is Event.StripeSettings.Enabled -> stripeSettings.locationId
            }

        // TODO we should probably check if the reader is already connected
        if (stripe.isInitialized()) {
            logger.w("Stripe Terminal is already initialized, skipping initialization")
            reduce { state.copy(initializationState = State.Finished) }
            return@intent
        }

        reduce { state.copy(initializationState = State.CheckingPermissions) }

        @Suppress("SwallowedException")
        try {
            permissionsController.providePermission(Permission.LOCATION)
        } catch (denied: DeniedException) {
            // TODO do we need to distinguish between denied and permanently denied?
            reduce { state.copy(initializationState = State.Error.GeolocationPermissionDenied) }
            return@intent
        } catch (cancelled: RequestCanceledException) {
            reduce { state.copy(initializationState = State.Error.GeolocationPermissionDenied) }
            return@intent
        }

        if (!stripe.isGeoLocationEnabled()) {
            logger.i("GPS not enabled")
            reduce { state.copy(initializationState = State.Error.GeolocationNotEnabled) }
            return@intent
        }

        if (!stripe.isNfcEnabled()) {
            logger.i("NFC not enabled")
            reduce { state.copy(initializationState = State.Error.NfcNotEnabled) }
            return@intent
        }

        reduce { state.copy(initializationState = State.InitializingTerminal) }

        try {
            stripe.initialize()
        } catch (e: StripeException) {
            logger.e("Failed to initialize terminal", e)
            reduce { state.copy(initializationState = State.Error.TerminalInitializationFailed) }
            return@intent
        }

        reduce { state.copy(initializationState = State.ConnectingReader) }

        try {
            stripe.connectLocalReader(locationId)
        } catch (e: StripeException) {
            logger.e("Failed to connect to local reader", e)
            reduce { state.copy(initializationState = State.Error.ReaderConnectionFailed) }
            return@intent
        }

        reduce { state.copy(initializationState = State.Finished) }
    }

    fun onContinueClick() = intent {
        navigator.popUpToRoot()
    }
}
