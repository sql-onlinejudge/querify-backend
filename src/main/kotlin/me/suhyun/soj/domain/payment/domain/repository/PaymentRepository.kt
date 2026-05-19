package me.suhyun.soj.domain.payment.domain.repository

import me.suhyun.soj.domain.payment.domain.model.Payment
import me.suhyun.soj.domain.payment.domain.model.enums.PaymentStatus

interface PaymentRepository {
    fun save(payment: Payment): Payment
    fun findByOrderId(orderId: String): Payment?
    fun updateStatus(id: Long, status: PaymentStatus, paymentKey: String? = null, subscriptionId: Long? = null): Boolean
}
