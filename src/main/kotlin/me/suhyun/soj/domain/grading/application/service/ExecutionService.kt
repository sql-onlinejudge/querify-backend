package me.suhyun.soj.domain.grading.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import me.suhyun.soj.domain.grading.exception.QueryExecutionException
import me.suhyun.soj.domain.grading.exception.QueryTimeoutException
import me.suhyun.soj.domain.grading.infrastructure.QueryExecutor
import me.suhyun.soj.domain.grading.infrastructure.QueryValidator
import me.suhyun.soj.domain.grading.infrastructure.sse.RunSseEmitterService
import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.problem.exception.ProblemErrorCode
import me.suhyun.soj.domain.run.domain.model.RunResult
import me.suhyun.soj.domain.run.domain.model.enums.RunStatus
import me.suhyun.soj.domain.run.domain.repository.RunRepository
import me.suhyun.soj.domain.run.exception.RunErrorCode
import me.suhyun.soj.domain.run.presentation.response.RunResultResponse
import me.suhyun.soj.domain.testcase.domain.repository.TestCaseRepository
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ExecutionService(
    private val runRepository: RunRepository,
    private val problemRepository: ProblemRepository,
    private val testCaseRepository: TestCaseRepository,
    private val queryExecutor: QueryExecutor,
    private val queryValidator: QueryValidator,
    private val runSseEmitterService: RunSseEmitterService,
    private val objectMapper: ObjectMapper,
) {
    fun execute(runId: Long) {
        val run = runRepository.findById(runId)
            ?: throw BusinessException(RunErrorCode.RUN_NOT_FOUND)

        val problem = problemRepository.findById(run.problemId)
            ?: throw BusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND)

        val testCases = testCaseRepository.findAllByProblemId(problem.id!!)

        runRepository.updateResult(runId, RunStatus.IN_PROGRESS, null, null)
        runSseEmitterService.send(runId, RunStatus.IN_PROGRESS, null, null)

        try {
            queryValidator.validate(run.query)

            val actualResults = mutableMapOf<Long, String>()
            for (testCase in testCases) {
                val actualResult = queryExecutor.execute(
                    schemaSql = problem.schemaSql,
                    initSql = testCase.initSql,
                    query = run.query,
                    timeoutMs = problem.timeLimit
                )
                actualResults[testCase.id!!] = actualResult
            }

            val resultJson = objectMapper.writeValueAsString(actualResults)
            runRepository.updateResult(runId, RunStatus.COMPLETED, resultJson, null)

            val responses = actualResults.map { (testCaseId, tsv) ->
                val parsed = RunResult.parse(tsv)
                RunResultResponse(
                    testCaseId = testCaseId,
                    columns = parsed.columns,
                    rows = parsed.rows,
                    errorMessage = null
                )
            }
            runSseEmitterService.send(runId, RunStatus.COMPLETED, responses, null)
        } catch (e: BusinessException) {
            runRepository.updateResult(runId, RunStatus.FAILED, null, e.message)
            runSseEmitterService.send(runId, RunStatus.FAILED, null, e.message)
        } catch (e: QueryTimeoutException) {
            runRepository.updateResult(runId, RunStatus.FAILED, null, "시간 초과")
            runSseEmitterService.send(runId, RunStatus.FAILED, null, "시간 초과")
        } catch (e: QueryExecutionException) {
            runRepository.updateResult(runId, RunStatus.FAILED, null, e.message)
            runSseEmitterService.send(runId, RunStatus.FAILED, null, e.message)
        }
    }
}
