package me.suhyun.soj.global.log.event

data class TrackEventRequest(
    val eventType: EventType,
    val targetId: String? = null,
    val metadata: Map<String, Any>? = null
)
