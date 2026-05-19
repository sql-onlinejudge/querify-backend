package me.suhyun.soj.domain.subscription.domain.entity

import me.suhyun.soj.domain.subscription.domain.model.enums.SubscriptionStatus
import me.suhyun.soj.global.common.entity.BaseTable
import org.jetbrains.exposed.sql.javatime.datetime

@Suppress("MagicNumber")
object SubscriptionTable : BaseTable("subscriptions") {
    val userId = varchar("user_id", 36)
    val status = enumerationByName<SubscriptionStatus>("status", 20)
    val startedAt = datetime("started_at")
    val expiresAt = datetime("expires_at")
}
