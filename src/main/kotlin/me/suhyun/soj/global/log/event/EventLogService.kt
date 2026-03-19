package me.suhyun.soj.global.log.event

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EventLogService(
    private val eventLogRepository: EventLogRepository
) {

    @Async
    fun log(
        userId: String?,
        eventType: EventType,
        targetId: String? = null,
        metadata: Map<String, Any>? = null,
        ipAddress: String? = null,
        userAgent: String? = null
    ) {
        runCatching {
            eventLogRepository.save(
                EventLogDocument(
                    userId = userId,
                    eventType = eventType,
                    page = null,
                    targetId = targetId,
                    metadata = metadata,
                    ipAddress = ipAddress,
                    userAgent = userAgent
                )
            )
        }
    }
}
