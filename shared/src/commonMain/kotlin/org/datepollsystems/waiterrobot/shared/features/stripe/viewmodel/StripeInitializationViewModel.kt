package org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel

import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeException
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeProvider
import org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel.StripeInitializationState.Step
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class StripeInitializationViewModel internal constructor(
    private val stripe: StripeProvider,
    val permissionsController: PermissionsController
) : AbstractViewModel<StripeInitializationState, StripeInitializationEffect>(
    StripeInitializationState()
) {
    private lateinit var locationId: String

    override suspend fun SimpleSyntax<
        StripeInitializationState,
        NavOrViewModelEffect<StripeInitializationEffect>
        >.onCreate() {
        setLocationId()
    }

    fun startInitialization() = intent {
        if (!setLocationId()) return@intent
        reduce { state.copy(isLoading = true) }

        // TODO maybe this can be changed to COARSE_LOCATION?
        if (permissionsController.isPermissionGranted(Permission.LOCATION)) {
            enableGeoLocation()
        } else {
            reduce { state.copy(step = Step.GrantLocationPermission, isLoading = false) }
        }
    }

    fun enableGeoLocation() = intent {
        reduce { state.copy(isLoading = true) }
        if (stripe.isGeoLocationEnabled()) {
            enableNfc()
        } else {
            reduce { state.copy(step = Step.EnableGeoLocation, isLoading = false) }
        }
    }

    // TODO this needs savedStateHandle (otherwise after returning from the settings it we will start from beginning
    fun enableNfc() = intent {
        reduce { state.copy(isLoading = true) }
        if (stripe.isNfcEnabled()) {
            initializeAndConnectTerminal()
        } else {
            reduce { state.copy(step = Step.EnableNfc, isLoading = false) }
        }
    }

    fun initializeAndConnectTerminal() = intent {
        reduce { state.copy(isLoading = true) }
        if (!stripe.isInitialized()) {
            try {
                stripe.initialize()
            } catch (e: StripeException) {
                logger.e("Failed to initialize terminal (${e.stripeErrorCode})", e)
                reduce {
                    state.copy(
                        step = Step.Error.TerminalInitializationFailed,
                        isLoading = false
                    )
                }
                return@intent
            }
        }

        if (!stripe.connectedToReader.value) {
            try {
                stripe.connectLocalReader(locationId)
            } catch (e: StripeException) {
                logger.e("Failed to connect to local reader (${e.stripeErrorCode})", e)
                reduce { state.copy(step = Step.Error.ReaderConnectionFailed, isLoading = false) }
                return@intent
            }
        }

        reduce { state.copy(step = Step.Finished, isLoading = false) }
    }

    fun grantLocationPermission() = intent {
        reduce { state.copy(isLoading = true) }
        @Suppress("SwallowedException")
        try {
            permissionsController.providePermission(Permission.LOCATION)
            enableGeoLocation()
        } catch (denied: DeniedException) {
            reduce { state.copy(step = Step.Error.GeolocationPermissionDenied, isLoading = false) }
        } catch (cancelled: RequestCanceledException) {
            reduce { state.copy(step = Step.Error.GeolocationPermissionDenied, isLoading = false) }
        }
    }

    fun onContinueClick(skipInit: Boolean) = intent {
        if (skipInit) {
            CommonApp.settings.enableContactlessPayment = false
        }

        navigator.replaceRoot(CommonApp.getNextRootScreen())
    }

    private suspend fun SimpleSyntax<
        StripeInitializationState,
        NavOrViewModelEffect<StripeInitializationEffect>
        >.setLocationId(): Boolean {
        when (val stripeSettings = CommonApp.settings.selectedEvent?.stripeSettings) {
            null -> {
                logger.w("Wanted to connect to local reader, but no event was selected")
                navigator.replaceRoot(CommonApp.getNextRootScreen())
            }

            Event.StripeSettings.Disabled -> {
                reduce { state.copy(step = Step.Error.StripeDisabledForEvent, isLoading = false) }
            }

            is Event.StripeSettings.Enabled -> {
                locationId = stripeSettings.locationId
                return true
            }
        }

        return false
    }
}
