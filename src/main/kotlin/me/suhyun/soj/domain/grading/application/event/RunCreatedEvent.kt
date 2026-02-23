package me.suhyun.soj.domain.grading.application.event

data class RunCreatedEvent(
    val runId: Long,
    val query: String
)