package me.suhyun.soj.domain.problem.presentation

import jakarta.validation.Valid
import me.suhyun.soj.domain.problem.application.service.ProblemService
import me.suhyun.soj.domain.problem.presentation.request.CreateProblemRequest
import me.suhyun.soj.domain.problem.presentation.request.UpdateProblemRequest
import me.suhyun.soj.domain.problem.presentation.response.ProblemDetailResponse
import me.suhyun.soj.domain.problem.presentation.response.ProblemResponse
import me.suhyun.soj.global.dto.PageResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/problems")
class ProblemController(
    private val problemService: ProblemService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProblem(
        @Valid @RequestBody request: CreateProblemRequest
    ): ProblemDetailResponse {
        val problem = problemService.createProblem(request)
        return ProblemDetailResponse.from(problem)
    }

    @GetMapping("/{id}")
    fun getProblem(@PathVariable id: Long): ProblemDetailResponse {
        val problem = problemService.getProblem(id)
        return ProblemDetailResponse.from(problem)
    }

    @GetMapping
    fun getProblemList(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) difficulty: Int?,
        @RequestParam(required = false) keyword: String?,
        @RequestParam(defaultValue = "id") sortBy: String,
        @RequestParam(defaultValue = "DESC") sortDirection: String
    ): PageResponse<ProblemResponse> {
        val result = problemService.getProblemList(
            page = page,
            size = size,
            difficulty = difficulty,
            keyword = keyword,
            sortBy = sortBy,
            sortDirection = sortDirection
        )
        return PageResponse.of(
            content = result.content.map { ProblemResponse.from(it) },
            page = result.page,
            size = result.size,
            totalElements = result.totalElements
        )
    }

    @PutMapping("/{id}")
    fun updateProblem(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateProblemRequest
    ): ProblemDetailResponse {
        val problem = problemService.updateProblem(id, request)
        return ProblemDetailResponse.from(problem)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProblem(@PathVariable id: Long) {
        problemService.deleteProblem(id)
    }
}
