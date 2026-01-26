package me.suhyun.soj.domain.grading.infrastructure.sse

import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Service
class SseEmitterService {

    private val emitters = ConcurrentHashMap<Long, SseEmitter>()

    companion object {
        private const val TIMEOUT = 60_000L
    }

    fun create(submissionId: Long): SseEmitter {
        val emitter = SseEmitter(TIMEOUT)

        emitter.onCompletion { emitters.remove(submissionId) }
        emitter.onTimeout { emitters.remove(submissionId) }
        emitter.onError { emitters.remove(submissionId) }

        emitters[submissionId] = emitter

        return emitter
    }

    fun send(submissionId: Long, status: SubmissionStatus, verdict: SubmissionVerdict?) {
        val emitter = emitters[submissionId] ?: return

        try {
            val event = GradingResultEvent(
                submissionId = submissionId,
                status = status.name,
                verdict = verdict?.name
            )

            emitter.send(
                SseEmitter.event()
                    .name("grading-result")
                    .data(event)
            )

            if (status == SubmissionStatus.COMPLETED) {
                emitter.complete()
                emitters.remove(submissionId)
            }
        } catch (e: Exception) {
            emitters.remove(submissionId)
        }
    }

    data class GradingResultEvent(
        val submissionId: Long,
        val status: String,
        val verdict: String?
    )
}
