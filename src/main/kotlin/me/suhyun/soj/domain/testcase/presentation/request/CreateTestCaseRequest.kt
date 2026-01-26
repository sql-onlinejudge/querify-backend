package me.suhyun.soj.domain.testcase.presentation.request

import jakarta.validation.constraints.NotBlank

data class CreateTestCaseRequest(
    val initSql: String?,

    @field:NotBlank
    val answer: String
)
