package me.suhyun.soj.domain.grading.infrastructure.sse

import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SseEmitterServiceTest {

    private lateinit var sseEmitterService: SseEmitterService

    @BeforeEach
    fun setUp() {
        sseEmitterService = SseEmitterService()
    }

    @Nested
    inner class Create {

        @Test
        fun `should create emitter for submission`() {
            val submissionId = 1L

            val emitter = sseEmitterService.create(submissionId)

            assertThat(emitter).isNotNull()
        }

        @Test
        fun `should create different emitters for different submissions`() {
            val emitter1 = sseEmitterService.create(1L)
            val emitter2 = sseEmitterService.create(2L)

            assertThat(emitter1).isNotSameAs(emitter2)
        }
    }

    @Nested
    inner class Send {

        @Test
        fun `should not throw when emitter does not exist`() {
            sseEmitterService.send(999L, SubmissionStatus.COMPLETED, SubmissionVerdict.ACCEPTED)
        }

        @Test
        fun `should send event to existing emitter`() {
            val submissionId = 1L
            sseEmitterService.create(submissionId)

            sseEmitterService.send(submissionId, SubmissionStatus.RUNNING, null)
        }
    }
}
