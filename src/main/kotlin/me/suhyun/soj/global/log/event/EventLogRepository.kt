package me.suhyun.soj.global.log.event

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface EventLogRepository : ElasticsearchRepository<EventLogDocument, String> {
    fun findByUserId(userId: String): List<EventLogDocument>
    fun findByEventType(eventType: EventType): List<EventLogDocument>
    fun findByUserIdAndEventType(userId: String, eventType: EventType): List<EventLogDocument>
}
