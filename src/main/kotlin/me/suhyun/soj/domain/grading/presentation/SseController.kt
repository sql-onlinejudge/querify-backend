package me.suhyun.soj.domain.grading.presentation

import me.suhyun.soj.domain.grading.infrastructure.sse.RunSseEmitterService
import me.suhyun.soj.domain.grading.infrastructure.sse.SseEmitterService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class SseController(
    private val sseEmitterService: SseEmitterService,
    private val runSseEmitterService: RunSseEmitterService
) {

    @GetMapping("/problems/{problemId}/submissions/{submissionId}/events", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun subscribeSubmission(@PathVariable problemId: Long, @PathVariable submissionId: Long): SseEmitter {
        return sseEmitterService.create(submissionId)
    }

    @GetMapping("/problems/{problemId}/runs/{runId}/events", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun subscribeRun(@PathVariable problemId: Long, @PathVariable runId: Long): SseEmitter {
        return runSseEmitterService.create(runId)
    }
}
