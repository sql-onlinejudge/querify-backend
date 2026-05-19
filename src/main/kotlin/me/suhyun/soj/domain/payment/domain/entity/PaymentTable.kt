package me.suhyun.soj.domain.payment.domain.entity

import me.suhyun.soj.domain.payment.domain.model.enums.PaymentStatus
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

@Suppress("MagicNumber")
object PaymentTable : LongIdTable("payments") {
    val userId = varchar("user_id", 36)
    val orderId = varchar("order_id", 64).uniqueIndex()
    val paymentKey = varchar("payment_key", 200).nullable()
    val amount = integer("amount")
    val status = enumerationByName<PaymentStatus>("status", 20)
    val subscriptionId = long("subscription_id").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
}
