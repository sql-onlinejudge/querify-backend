package me.suhyun.soj.domain.grading.application.event

import me.suhyun.soj.domain.grading.exception.GradingErrorCode
import me.suhyun.soj.domain.grading.infrastructure.kafka.ExecutionKafkaProducer
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class RunEventLister(
    private val executionKafkaProducer: ExecutionKafkaProducer
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleRunCreated(event: RunCreatedEvent) {
        try {
            executionKafkaProducer.send(event.runId)
        } catch (e: Exception) {
            throw BusinessException(GradingErrorCode.GRADING_SERVER_UNAVAILABLE)
        }
    }
}
