package me.suhyun.soj.domain.payment.domain.repository

import me.suhyun.soj.domain.payment.domain.entity.PaymentEntity
import me.suhyun.soj.domain.payment.domain.entity.PaymentTable
import me.suhyun.soj.domain.payment.domain.model.Payment
import me.suhyun.soj.domain.payment.domain.model.enums.PaymentStatus
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class PaymentRepositoryImpl : PaymentRepository {

    override fun save(payment: Payment): Payment {
        val entity = PaymentEntity.new {
            this.userId = payment.userId.toString()
            this.orderId = payment.orderId
            this.paymentKey = payment.paymentKey
            this.amount = payment.amount
            this.status = payment.status
            this.subscriptionId = payment.subscriptionId
            this.createdAt = LocalDateTime.now()
        }
        return Payment.from(entity)
    }

    override fun findByOrderId(orderId: String): Payment? {
        return PaymentTable.selectAll()
            .where { PaymentTable.orderId eq orderId }
            .firstOrNull()
            ?.let { Payment.from(PaymentEntity.wrapRow(it)) }
    }

    override fun updateStatus(id: Long, status: PaymentStatus, paymentKey: String?, subscriptionId: Long?): Boolean {
        val entity = PaymentEntity.findById(id) ?: return false
        entity.status = status
        paymentKey?.let { entity.paymentKey = it }
        subscriptionId?.let { entity.subscriptionId = it }
        entity.updatedAt = LocalDateTime.now()
        return true
    }
}
