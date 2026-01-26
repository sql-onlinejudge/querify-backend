package me.suhyun.soj.domain.submission.presentation.request

import jakarta.validation.constraints.NotBlank

data class SubmitRequest(
    @field:NotBlank
    val query: String
)
