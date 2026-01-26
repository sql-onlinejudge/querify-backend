package me.suhyun.soj.domain.grading.application.event

import me.suhyun.soj.domain.grading.infrastructure.kafka.GradingKafkaProducer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class SubmissionEventListenerTest {

    @Mock
    private lateinit var gradingKafkaProducer: GradingKafkaProducer

    private lateinit var submissionEventListener: SubmissionEventListener

    @BeforeEach
    fun setUp() {
        submissionEventListener = SubmissionEventListener(gradingKafkaProducer)
    }

    @Test
    fun `should send kafka message when submission created event received`() {
        val submissionId = 1L
        val event = SubmissionCreatedEvent(submissionId)

        submissionEventListener.handleSubmissionCreated(event)

        verify(gradingKafkaProducer).send(submissionId)
    }
}
