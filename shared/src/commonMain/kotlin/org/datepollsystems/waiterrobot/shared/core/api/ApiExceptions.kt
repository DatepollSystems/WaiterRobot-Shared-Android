package org.datepollsystems.waiterrobot.shared.core.api

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("codeName")
internal sealed class ApiException : Exception() {
    final override lateinit var message: String
        private set

    @JsonNames("code", "httpCode")
    var httpCode = 0 // TODO Will be "httpCode" in next version
        private set
    lateinit var codeName: String
        private set

    // This is used as a fallback when the client does not know about a specific codeName
    @Serializable
    class Generic : ApiException()

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
    @SerialName("SOLD_OUT")
    class ProductSoldOut(val productId: Long) : ApiException()

    @Serializable
    @SerialName("ACCOUNT_NOT_ACTIVATED")
    class AccountNotActivated : ApiException()

    @Serializable
    @SerialName("WAITER_AUTH_TOKEN_INCORRECT")
    class WaiterTokenIncorrect : ApiException()

    @Serializable
    @SerialName("WAITER_CREATE_TOKEN_INCORRECT")
    class WaiterCreateTokenIncorrect : ApiException()
}
