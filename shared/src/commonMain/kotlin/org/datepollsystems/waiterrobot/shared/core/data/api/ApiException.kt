package org.datepollsystems.waiterrobot.shared.core.data.api

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.datepollsystems.waiterrobot.shared.utils.Cents

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("codeName")
internal sealed class ApiException : Exception() {
    final override lateinit var message: String
        private set

    var httpCode = 0
        private set
    lateinit var codeName: String
        private set

    // This is used as a fallback when the client does not know about a specific codeName
    @Serializable
    class Generic : ApiException()

    @Serializable
    @SerialName("CREDENTIALS_INCORRECT")
    class CredentialsIncorrect : ApiException()

    @Serializable
    @SerialName("NOT_FOUND")
    class NotFound(val entityId: Long? = null) : ApiException()

    @Serializable
    @SerialName("CONFLICT")
    class EntityAlreadyExists : ApiException()

    @Serializable
    @SerialName("FORBIDDEN")
    class Forbidden : ApiException()

    @Serializable
    @SerialName("UNAUTHORIZED")
    class Unauthorized : ApiException()

    @Serializable
    @SerialName("BAD_REQUEST")
    class BadRequest : ApiException()

    @Serializable
    @SerialName("SERVICE_UNAVAILABLE")
    class ServiceUnavailable : ApiException()

    @Serializable
    @SerialName("SOLD_OUT")
    class ProductSoldOut(val productId: Long) : ApiException()

    @Serializable
    @SerialName("TOO_SMALL_STOCK")
    class ProductStockToLow(val productId: Long, val remaining: Int = 0) : ApiException()

    @Serializable
    @SerialName("ACCOUNT_NOT_ACTIVATED")
    class AccountNotActivated : ApiException()

    @Serializable
    @SerialName("WAITER_AUTH_TOKEN_INCORRECT")
    class WaiterTokenIncorrect : ApiException()

    @Serializable
    @SerialName("WAITER_CREATE_TOKEN_INCORRECT")
    class WaiterCreateTokenIncorrect : ApiException()

    @Serializable
    @SerialName("APP_VERSION_TOO_OLD")
    class AppVersionTooOld : ApiException()

    @Serializable
    @SerialName("BILL_AMOUNT_TOO_LOW")
    class BillAmountTooLow(val minAmount: Cents) : ApiException()

    @Serializable
    @SerialName("STRIPE_NOT_ACTIVATED")
    class StripeNotActivated(val eventId: Long) : ApiException()

    @Serializable
    @SerialName("STRIPE_DISABLED")
    class StripeDisabled : ApiException()

    @Serializable
    @SerialName("ORDER_ALREADY_SUBMITTED")
    class OrderAlreadySubmitted : ApiException()
}
