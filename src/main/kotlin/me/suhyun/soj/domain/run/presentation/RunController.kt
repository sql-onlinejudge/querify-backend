package me.suhyun.soj.domain.run.presentation

import jakarta.validation.Valid
import me.suhyun.soj.domain.run.application.service.RunService
import me.suhyun.soj.domain.run.presentation.request.CreateRunRequest
import me.suhyun.soj.domain.run.presentation.response.RunResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/problems/{problemId}/runs")
class RunController(
    private val runService: RunService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @PathVariable problemId: Long,
        @Valid @RequestBody request: CreateRunRequest
    ): CreateRunResponse {
        val runId = runService.createRun(problemId, request)
        return CreateRunResponse(runId)
    }

    @GetMapping("/{runId}")
    fun getResult(
        @PathVariable problemId: Long,
        @PathVariable runId: Long
    ): RunResponse {
        return runService.getResult(runId)
    }

    data class CreateRunResponse(val runId: Long)
}
