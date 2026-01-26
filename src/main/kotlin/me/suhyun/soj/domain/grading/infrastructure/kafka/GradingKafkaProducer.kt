package me.suhyun.soj.domain.grading.infrastructure.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class GradingKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    companion object {
        const val TOPIC = "grading-requests"
    }

    fun send(submissionId: Long) {
        val event = GradingEvent(submissionId)
        val message = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(TOPIC, submissionId.toString(), message)
    }
}
