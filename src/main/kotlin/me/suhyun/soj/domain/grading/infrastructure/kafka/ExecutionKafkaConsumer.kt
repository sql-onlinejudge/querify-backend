package me.suhyun.soj.domain.grading.infrastructure.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import me.suhyun.soj.domain.grading.application.service.ExecutionService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ExecutionKafkaConsumer(
    private val executionService: ExecutionService,
    private val objectMapper: ObjectMapper
) {
    @KafkaListener(topics = [ExecutionKafkaProducer.TOPIC], groupId = "execution-group")
    fun consume(message: String) {
        val event = objectMapper.readValue(message, ExecutionEvent::class.java)
        executionService.execute(event.runId)
    }
}
