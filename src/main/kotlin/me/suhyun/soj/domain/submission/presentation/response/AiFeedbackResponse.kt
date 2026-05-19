package me.suhyun.soj.domain.submission.presentation.response

data class AiFeedbackResponse(
    val hint: String,
    val improvement: String,
    val explanation: String
)
