package me.suhyun.soj.domain.grading.infrastructure.sse

import me.suhyun.soj.domain.run.domain.model.enums.RunStatus
import me.suhyun.soj.domain.run.presentation.response.RunResponse
import me.suhyun.soj.domain.run.presentation.response.RunResultResponse
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Service
class RunSseEmitterService {

    private val emitters = ConcurrentHashMap<Long, SseEmitter>()

    companion object {
        private const val TIMEOUT = 60_000L
    }

    fun create(runId: Long): SseEmitter {
        val emitter = SseEmitter(TIMEOUT)
        emitter.onCompletion { emitters.remove(runId) }
        emitter.onTimeout { emitters.remove(runId) }
        emitter.onError { emitters.remove(runId) }
        emitters[runId] = emitter
        return emitter
    }

    fun send(runId: Long, status: RunStatus, results: List<RunResultResponse>?, errorMessage: String?) {
        val emitter = emitters[runId] ?: return
        try {
            val responseResults = results ?: errorMessage?.let {
                listOf(RunResultResponse(testCaseId = -1, columns = emptyList(), rows = emptyList(), errorMessage = it))
            }

            val event = RunResponse(runId = runId, status = status.name, results = responseResults)

            emitter.send(
                SseEmitter.event()
                    .name("run-result")
                    .data(event)
            )

            if (status == RunStatus.COMPLETED || status == RunStatus.FAILED) {
                emitter.complete()
                emitters.remove(runId)
            }
        } catch (e: Exception) {
            emitters.remove(runId)
        }
    }
}
