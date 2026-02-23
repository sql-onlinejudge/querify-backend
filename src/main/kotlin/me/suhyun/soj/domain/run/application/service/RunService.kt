package me.suhyun.soj.domain.run.application.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import me.suhyun.soj.domain.grading.application.event.RunCreatedEvent
import me.suhyun.soj.domain.run.domain.model.Run
import me.suhyun.soj.domain.run.domain.model.RunResult
import me.suhyun.soj.domain.run.domain.model.enums.RunStatus
import me.suhyun.soj.domain.run.domain.repository.RunRepository
import me.suhyun.soj.domain.run.exception.RunErrorCode
import me.suhyun.soj.domain.run.presentation.request.CreateRunRequest
import me.suhyun.soj.domain.run.presentation.response.RunResponse
import me.suhyun.soj.domain.run.presentation.response.RunResultResponse
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class RunService(
    private val runRepository: RunRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper,
) {
    fun createRun(problemId: Long, request: CreateRunRequest): Long {
        val saved = runRepository.save(
            Run(
                id = null,
                problemId = problemId,
                query = request.query,
                status = RunStatus.PENDING,
                result = null,
                errorMessage = null,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null
            )
        )
        eventPublisher.publishEvent(RunCreatedEvent(saved.id!!, saved.query))
        return saved.id
    }

    @Transactional(readOnly = true)
    fun getResult(runId: Long): RunResponse {
        val run = runRepository.findById(runId)
            ?: throw BusinessException(RunErrorCode.RUN_NOT_FOUND)

        val results = run.result?.let { json ->
            val tsvMap: Map<String, String> = objectMapper.readValue(json, object : TypeReference<Map<String, String>>() {})
            tsvMap.map { (testCaseIdStr, tsv) ->
                val testCaseId = testCaseIdStr.toLong()
                val parsed = RunResult.parse(tsv)
                RunResultResponse(
                    testCaseId = testCaseId,
                    columns = parsed.columns,
                    rows = parsed.rows,
                    errorMessage = null
                )
            }
        }

        return RunResponse(
            runId = run.id!!,
            status = run.status.name,
            results = results
        )
    }
}
