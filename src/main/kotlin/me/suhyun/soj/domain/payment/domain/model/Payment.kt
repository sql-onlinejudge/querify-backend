package me.suhyun.soj.domain.payment.domain.model

import me.suhyun.soj.domain.payment.domain.entity.PaymentEntity
import me.suhyun.soj.domain.payment.domain.model.enums.PaymentStatus
import java.time.LocalDateTime
import java.util.UUID

data class Payment(
    val id: Long?,
    val userId: UUID,
    val orderId: String,
    val paymentKey: String?,
    val amount: Int,
    val status: PaymentStatus,
    val subscriptionId: Long?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(entity: PaymentEntity) = Payment(
            id = entity.id.value,
            userId = UUID.fromString(entity.userId),
            orderId = entity.orderId,
            paymentKey = entity.paymentKey,
            amount = entity.amount,
            status = entity.status,
            subscriptionId = entity.subscriptionId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}
