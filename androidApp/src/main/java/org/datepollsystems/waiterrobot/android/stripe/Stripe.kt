package org.datepollsystems.waiterrobot.android.stripe

import android.content.Context
import android.location.LocationManager
import com.stripe.stripeterminal.Terminal
import com.stripe.stripeterminal.external.callable.TerminalListener
import com.stripe.stripeterminal.external.models.CollectConfiguration
import com.stripe.stripeterminal.external.models.ConnectionConfiguration
import com.stripe.stripeterminal.external.models.ConnectionStatus
import com.stripe.stripeterminal.external.models.DiscoveryConfiguration
import com.stripe.stripeterminal.external.models.PaymentStatus
import com.stripe.stripeterminal.external.models.Reader
import com.stripe.stripeterminal.external.models.TerminalException
import com.stripe.stripeterminal.log.LogLevel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.datepollsystems.waiterrobot.android.BuildConfig
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.di.injectLoggerForClass
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeProvider
import org.datepollsystems.waiterrobot.shared.features.stripe.api.models.PaymentIntent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object Stripe : KoinComponent, TerminalListener, StripeProvider {
    private val logger by injectLoggerForClass()
    private val context by inject<Context>()

    @Suppress("ReturnCount")
    suspend fun connectLocalReader() {
        if (!isGeoLocationEnabled()) {
            logger.i("GPS not enabled")
            // TODO notify user
            return
        }

        if (isInitialized()) {
            logger.i("Terminal is already initialized")
            return
        }

        val locationId = CommonApp.settings.stripeLocationId
        if (locationId == null) {
            logger.w("Wanted to connect to local reader, but locationId was null")
            return
        }

        try {
            Terminal.initTerminal(
                context = context,
                logLevel = LogLevel.VERBOSE,
                tokenProvider = getKoin().get(),
                listener = this
            )
        } catch (e: TerminalException) {
            logger.e("Terminal initialization failed", e)
            return
        }

        val discoverConfig = DiscoveryConfiguration.LocalMobileDiscoveryConfiguration(
            isSimulated = BuildConfig.DEBUG // In debug mode only simulated readers are supported
        )

        var reader = try {
            Terminal.getInstance().discoverReaders(discoverConfig).first().firstOrNull()
        } catch (e: TerminalException) {
            logger.e("Reader discovery failed", e)
            return
        }
        if (reader == null) {
            logger.w("No reader found")
            return
        }

        val connectConfig = ConnectionConfiguration.LocalMobileConnectionConfiguration(
            locationId,
            autoReconnectOnUnexpectedDisconnect = true
        )

        reader = reader.connect(connectConfig)
        logger.i("Connected to reader: ${reader.id}")
    }

    // TODO error handling & retry
    suspend fun startPayment(clientSecret: String) {
        var paymentIntentResult: Result<com.stripe.stripeterminal.external.models.PaymentIntent>
        var tryCount = 0
        do {
            delay(tryCount * 500L)
            paymentIntentResult = runCatching {
                Terminal.getInstance().retrievePaymentIntent(clientSecret)
            }
        } while (paymentIntentResult.isFailure && ++tryCount < 3)

        val paymentIntent = paymentIntentResult.getOrThrow()

        val collectConfig = CollectConfiguration.Builder()
            .skipTipping(false) // TODO from settings
            .build()

        val collectedIntent = paymentIntent.collectPaymentMethod(collectConfig)

        collectedIntent.confirmPaymentIntent()
    }

    override fun onUnexpectedReaderDisconnect(reader: Reader) {
        // TODO handle (or is this already covered by autoReconnectOnUnexpectedDisconnect?)
        logger.w("Reader disconnected")
    }

    override fun onConnectionStatusChange(status: ConnectionStatus) {
        logger.i("Reader status changed to $status")
    }

    override fun onPaymentStatusChange(status: PaymentStatus) {
        logger.d("Payment status changed to $status")
    }

    override suspend fun initiatePayment(intent: PaymentIntent) {
        startPayment(intent.clientSecret)
    }

    override fun isGeoLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        return runCatching {
            locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }.getOrNull() ?: false
    }

    override fun isInitialized(): Boolean = Terminal.isInitialized()
}
