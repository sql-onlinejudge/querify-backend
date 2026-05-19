package me.suhyun.soj.domain.payment.infrastructure.toss

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "toss")
data class TossPaymentsProperties(
    val clientKey: String = "",
    val secretKey: String = "",
    val successUrl: String = "http://localhost:8080/payments/success",
    val failUrl: String = "http://localhost:8080/payments/fail"
)
