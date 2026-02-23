package me.suhyun.soj.domain.run.presentation.request

import jakarta.validation.constraints.NotBlank

data class CreateRunRequest(
    @field:NotBlank
    val query: String,
)
