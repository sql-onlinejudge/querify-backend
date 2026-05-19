package me.suhyun.soj.domain.subscription.domain.entity

import me.suhyun.soj.domain.subscription.domain.model.enums.SubscriptionStatus
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SubscriptionEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SubscriptionEntity>(SubscriptionTable)

    var userId by SubscriptionTable.userId
    var status by SubscriptionTable.status
    var startedAt by SubscriptionTable.startedAt
    var expiresAt by SubscriptionTable.expiresAt
    var createdAt by SubscriptionTable.createdAt
    var updatedAt by SubscriptionTable.updatedAt
    var deletedAt by SubscriptionTable.deletedAt
}
