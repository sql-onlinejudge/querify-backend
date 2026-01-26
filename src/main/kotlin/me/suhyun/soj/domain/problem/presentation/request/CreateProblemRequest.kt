package me.suhyun.soj.domain.problem.presentation.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
import me.suhyun.soj.domain.testcase.presentation.request.CreateTestCaseRequest

data class CreateProblemRequest(
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val description: String,

    @field:NotBlank
    val schemaSql: String,

    @field:Min(1)
    @field:Max(5)
    val difficulty: Int,

    @field:Positive
    val timeLimit: Int,

    val isOrderSensitive: Boolean = false,

    @field:NotEmpty
    val testcases: List<CreateTestCaseRequest>
)
