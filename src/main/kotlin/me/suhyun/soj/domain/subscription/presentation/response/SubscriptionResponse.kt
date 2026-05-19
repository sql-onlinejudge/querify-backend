package me.suhyun.soj.domain.subscription.presentation.response

import me.suhyun.soj.domain.subscription.domain.model.Subscription
import me.suhyun.soj.domain.subscription.domain.model.enums.SubscriptionStatus
import java.time.LocalDateTime

data class SubscriptionResponse(
    val id: Long?,
    val status: SubscriptionStatus,
    val startedAt: LocalDateTime,
    val expiresAt: LocalDateTime
) {
    companion object {
        fun from(subscription: Subscription) = SubscriptionResponse(
            id = subscription.id,
            status = subscription.status,
            startedAt = subscription.startedAt,
            expiresAt = subscription.expiresAt
        )
    }
}
