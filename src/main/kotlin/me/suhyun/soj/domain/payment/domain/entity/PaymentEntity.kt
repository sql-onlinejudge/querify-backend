package me.suhyun.soj.domain.payment.domain.entity

import me.suhyun.soj.domain.payment.domain.model.enums.PaymentStatus
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PaymentEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PaymentEntity>(PaymentTable)

    var userId by PaymentTable.userId
    var orderId by PaymentTable.orderId
    var paymentKey by PaymentTable.paymentKey
    var amount by PaymentTable.amount
    var status by PaymentTable.status
    var subscriptionId by PaymentTable.subscriptionId
    var createdAt by PaymentTable.createdAt
    var updatedAt by PaymentTable.updatedAt
}
