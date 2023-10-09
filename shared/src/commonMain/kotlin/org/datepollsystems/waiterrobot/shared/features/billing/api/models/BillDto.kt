package org.datepollsystems.waiterrobot.shared.features.billing.api.models

import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.data.api.RequestBodyDto
import org.datepollsystems.waiterrobot.shared.utils.Cents

@Serializable
internal class BillResponseDto(
    val tableId: Long,
    val tableNumber: Long,
    val products: List<BillItemDto>
) {
    @Serializable
    class BillItemDto(
        val id: Long,
        val amount: Int,
        val name: String,
        val pricePerPiece: Cents
    )
}

@Serializable
internal class PayBillRequestDto(
    val products: List<BillItemDto>
) : RequestBodyDto {
    @Serializable
    class BillItemDto(
        val id: Long,
        val amount: Int
    )
}

@Serializable
internal class PayBillResponseDto(
    val billId: Long,
    val tableId: Long,
    val tableNumber: Long,
    val priceSum: Cents
)
