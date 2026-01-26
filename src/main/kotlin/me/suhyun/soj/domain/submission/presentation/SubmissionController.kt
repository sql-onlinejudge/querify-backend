package me.suhyun.soj.domain.submission.presentation

import jakarta.validation.Valid
import me.suhyun.soj.domain.submission.application.service.SubmissionService
import me.suhyun.soj.domain.submission.presentation.request.SubmitRequest
import me.suhyun.soj.domain.submission.presentation.response.SubmissionResponse
import me.suhyun.soj.global.common.dto.PageResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/problems/{problemId}/submissions")
class SubmissionController(
    private val submissionService: SubmissionService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun submit(
        @PathVariable problemId: Long,
        @RequestHeader("X-User-Id") userId: String,
        @Valid @RequestBody request: SubmitRequest
    ) {
        submissionService.submit(problemId, UUID.fromString(userId), request)
    }

    @GetMapping
    fun findAll(
        @PathVariable problemId: Long,
        @RequestHeader(value = "X-User-Id", required = false) userId: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): PageResponse<SubmissionResponse> {
        return if (userId != null) {
            submissionService.findMyByProblemId(problemId, UUID.fromString(userId), page, size)
        } else {
            submissionService.findByProblemId(problemId, page, size)
        }
    }

    @GetMapping("/{submissionId}")
    fun findById(
        @PathVariable problemId: Long,
        @PathVariable submissionId: Long
    ): SubmissionResponse {
        return submissionService.findById(problemId, submissionId)
    }
}
