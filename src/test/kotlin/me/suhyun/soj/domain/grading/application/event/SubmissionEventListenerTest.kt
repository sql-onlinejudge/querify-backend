package me.suhyun.soj.domain.grading.application.event

import me.suhyun.soj.domain.grading.infrastructure.kafka.GradingKafkaProducer
import me.suhyun.soj.global.infrastructure.notification.Notifier
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

    @Mock
    private lateinit var notifier: Notifier

    private lateinit var submissionEventListener: SubmissionEventListener

    @BeforeEach
    fun setUp() {
        submissionEventListener = SubmissionEventListener(gradingKafkaProducer, notifier)
    }

    @Test
    fun `should send kafka message when submission created event received`() {
        val submissionId = 1L
        val query = "SELECT * FROM users"
        val event = SubmissionCreatedEvent(submissionId, query)

        submissionEventListener.handleSubmissionCreated(event)

        verify(gradingKafkaProducer).send(submissionId)
    }
}
