package org.datepollsystems.waiterrobot.shared.features.billing.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.data.api.RequestBodyDto
import org.datepollsystems.waiterrobot.shared.utils.Cents

@Serializable
internal class BillResponseDtoV1(
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
internal class PayBillRequestV1Dto(
    val tableId: Long,
    val products: List<BillItemDto>
) : RequestBodyDto {
    @Serializable
    class BillItemDto(
        val id: Long,
        val amount: Int
    )
}

@Serializable
internal class BillResponseDto(
    val implodedOrderProducts: List<BillItemDto>,
    val priceSum: Cents,
) {
    @Serializable
    class BillItemDto(
        val name: String,
        val priceSum: Cents,
        val pricePerPiece: Cents,
        val amount: Int,
        val productId: Long,
        val orderProductIds: List<Long>
    )
}

@Serializable
internal class PayBillRequestDto(
    val tableId: Long,
    val orderProducts: List<Long>,
    val unpaidReasonId: Long? = null,
) : RequestBodyDto

@Serializable
internal class PayBillResponseDto(
    val bill: Bill,
    val openBill: BillResponseDto,
) {
    @Serializable
    class Bill(
        val id: Long,
        val waiter: WaiterDto,
        val createdAt: Instant,
        val pricePaidSum: Int,
        val unpaidReason: UnpaidReasonDto?,
        val implodedBillProducts: List<BillItemDto>,
    ) {
        @Serializable
        class WaiterDto(
            val id: Long,
            val name: String,
        )

        @Serializable
        class UnpaidReasonDto(
            val id: Long,
            val reason: String,
            val description: String?,
            val isGlobal: Boolean,
        )

        @Serializable
        class BillItemDto(
            val name: String,
            val pricePaidSum: Cents,
            val pricePaidPerPiece: Cents,
            val productId: Long,
            val amount: Int,
            val billProductIds: List<Long>,
        )
    }
}
