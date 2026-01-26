package me.suhyun.soj.domain.grading.application.event

import me.suhyun.soj.domain.grading.infrastructure.kafka.GradingKafkaProducer
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class SubmissionEventListener(
    private val gradingKafkaProducer: GradingKafkaProducer
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleSubmissionCreated(event: SubmissionCreatedEvent) {
        gradingKafkaProducer.send(event.submissionId)
    }
}
