package me.suhyun.soj.domain.grading.infrastructure.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ExecutionKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    companion object {
        const val TOPIC = "execution-requests"
    }

    fun send(runId: Long) {
        val event = ExecutionEvent(runId)
        kafkaTemplate.send(TOPIC, runId.toString(), event)
    }
}
