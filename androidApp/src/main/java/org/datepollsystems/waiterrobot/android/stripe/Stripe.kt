package org.datepollsystems.waiterrobot.android.stripe

import android.content.Context
import android.location.LocationManager
import android.nfc.NfcAdapter
import com.stripe.stripeterminal.Terminal
import com.stripe.stripeterminal.external.callable.TerminalListener
import com.stripe.stripeterminal.external.models.CollectConfiguration
import com.stripe.stripeterminal.external.models.ConnectionConfiguration
import com.stripe.stripeterminal.external.models.ConnectionStatus
import com.stripe.stripeterminal.external.models.DiscoveryConfiguration
import com.stripe.stripeterminal.external.models.PaymentIntentStatus
import com.stripe.stripeterminal.external.models.PaymentStatus
import com.stripe.stripeterminal.external.models.Reader
import com.stripe.stripeterminal.external.models.TerminalException
import com.stripe.stripeterminal.log.LogLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import org.datepollsystems.waiterrobot.android.BuildConfig
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.di.injectLoggerForClass
import org.datepollsystems.waiterrobot.shared.features.billing.repository.GeoLocationDisabledException
import org.datepollsystems.waiterrobot.shared.features.billing.repository.NfcDisabledException
import org.datepollsystems.waiterrobot.shared.features.billing.repository.NoReaderFoundException
import org.datepollsystems.waiterrobot.shared.features.billing.repository.PaymentCanceledException
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeException
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeProvider
import org.datepollsystems.waiterrobot.shared.features.stripe.api.models.PaymentIntent
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

@Suppress("TooManyFunctions")
object Stripe : KoinComponent, TerminalListener, StripeProvider {
    private val logger by injectLoggerForClass()
    private val context by inject<Context>()

    private val _connectedToReader: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val connectedToReader: StateFlow<Boolean> get() = _connectedToReader

    override suspend fun collectPayment(intent: PaymentIntent): Boolean = try {
        val paymentIntent = Terminal.retrievePaymentIntent(intent.clientSecret)

        val collectConfig = CollectConfiguration.Builder()
            .skipTipping(false) // TODO add setting for this?
            .build()

        val collectedIntent = paymentIntent.collectPaymentMethod(collectConfig)

        collectedIntent.confirm().status == PaymentIntentStatus.SUCCEEDED
    } catch (e: TerminalException) {
        throw when (e.errorCode) {
            TerminalException.TerminalErrorCode.CANCELED ->
                PaymentCanceledException(e, e.errorCode.toLogString())

            TerminalException.TerminalErrorCode.LOCATION_SERVICES_DISABLED ->
                GeoLocationDisabledException(e, e.errorCode.toLogString())

            TerminalException.TerminalErrorCode.LOCAL_MOBILE_NFC_DISABLED ->
                NfcDisabledException(e, e.errorCode.toLogString())

            else -> e.toStripeException("Failed to initiate payment")
        }
    }

    override suspend fun cancelPayment(intent: PaymentIntent) {
        val paymentIntent = Terminal.retrievePaymentIntent(intent.clientSecret)
        paymentIntent.cancel()
    }

    override fun isGeoLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        return runCatching {
            locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }.getOrNull() ?: false
    }

    override fun isNfcEnabled(): Boolean = NfcAdapter.getDefaultAdapter(context)?.isEnabled ?: false

    override fun isInitialized(): Boolean = Terminal.isInitialized()

    override fun initialize(): Unit = try {
        Terminal.initTerminal(
            context = context,
            logLevel = LogLevel.VERBOSE,
            tokenProvider = get(),
            listener = this
        )
    } catch (e: TerminalException) {
        e.toStripeException("Failed to initialize terminal")
    }

    override suspend fun disconnectReader(): Unit = Terminal.disconnectReader()

    override suspend fun connectLocalReader(locationId: String) {
        // In debug mode only simulated readers can be used
        // And it seems like test locations can also only be used with simulated readers
        val discoverConfig = DiscoveryConfiguration.LocalMobileDiscoveryConfiguration(
            isSimulated = BuildConfig.DEBUG ||
                CommonApp.appInfo.appVersion.contains("lava", ignoreCase = true)
        )

        val reader = try {
            Terminal.discoverReaders(discoverConfig).first().firstOrNull()
                ?: throw NoReaderFoundException()
        } catch (e: TerminalException) {
            e.toStripeException("Reader discovery failed")
        }

        val connectConfig = ConnectionConfiguration.LocalMobileConnectionConfiguration(
            locationId,
            autoReconnectOnUnexpectedDisconnect = true
        )

        try {
            reader.connect(connectConfig)
        } catch (e: TerminalException) {
            e.toStripeException("Reader connection failed")
        }

        _connectedToReader.emit(true)
        logger.i("Connected to reader ${reader.id}")
    }

    override fun onUnexpectedReaderDisconnect(reader: Reader) {
        logger.w("Reader disconnected")
    }

    override fun onConnectionStatusChange(status: ConnectionStatus) {
        logger.i("Reader status changed to $status")
        _connectedToReader.value = status == ConnectionStatus.CONNECTED
    }

    override fun onPaymentStatusChange(status: PaymentStatus) {
        logger.d("Payment status changed to $status")
    }
}

fun TerminalException.toStripeException(message: String): Nothing = throw StripeException(
    message = message,
    stripeErrorCode = this.errorCode.toLogString(),
    cause = this,
)
