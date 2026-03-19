package me.suhyun.soj.global.log.event

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/events")
class EventController(
    private val eventLogService: EventLogService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun track(
        @RequestBody request: TrackEventRequest,
        httpRequest: HttpServletRequest
    ) {
        val userId = SecurityContextHolder.getContext().authentication?.principal as? UUID
        eventLogService.log(
            userId = userId?.toString(),
            eventType = request.eventType,
            targetId = request.targetId,
            metadata = request.metadata,
            ipAddress = httpRequest.remoteAddr,
            userAgent = httpRequest.getHeader("User-Agent")
        )
    }
}
