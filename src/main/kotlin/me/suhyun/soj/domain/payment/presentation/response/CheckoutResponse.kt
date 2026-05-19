package me.suhyun.soj.domain.payment.presentation.response

data class CheckoutResponse(
    val orderId: String,
    val amount: Int,
    val clientKey: String,
    val successUrl: String,
    val failUrl: String
)
