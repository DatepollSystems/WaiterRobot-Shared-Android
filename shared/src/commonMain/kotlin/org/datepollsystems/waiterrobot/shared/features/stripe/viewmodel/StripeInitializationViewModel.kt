package org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel

import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeProvider
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class StripeInitializationViewModel internal constructor(
    private val stripe: StripeProvider,
    private val permissionsController: PermissionsController
) : AbstractViewModel<StripeInitializationState, StripeInitializationEffect>(
    StripeInitializationState()
) {
    fun enableStripe() = intent {
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
    }
}
