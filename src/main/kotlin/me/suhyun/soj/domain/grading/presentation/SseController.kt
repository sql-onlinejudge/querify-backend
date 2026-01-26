package me.suhyun.soj.domain.grading.presentation

import me.suhyun.soj.domain.grading.infrastructure.sse.SseEmitterService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/problems/{problemId}/submissions")
class SseController(
    private val sseEmitterService: SseEmitterService
) {

    @GetMapping("/{submissionId}/events", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun subscribe(@PathVariable problemId: Long, @PathVariable submissionId: Long): SseEmitter {
        return sseEmitterService.create(submissionId)
    }
}
