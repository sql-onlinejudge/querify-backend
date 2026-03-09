package me.suhyun.soj.domain.testcase.presentation

import jakarta.validation.Valid
import me.suhyun.soj.domain.testcase.application.service.TestCaseService
import me.suhyun.soj.domain.testcase.presentation.request.CreateTestCaseRequest
import me.suhyun.soj.domain.testcase.presentation.request.UpdateTestCaseRequest
import me.suhyun.soj.domain.testcase.presentation.response.TestCaseResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/problems/{problemId}/testcases")
class TestCaseController(
    private val testCaseService: TestCaseService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @PathVariable problemId: Long,
        @Valid @RequestBody request: CreateTestCaseRequest
    ) {
        testCaseService.create(problemId, request)
    }

    @GetMapping
    fun findAll(
        @PathVariable problemId: Long,
        @RequestParam(defaultValue = "true") isVisible: Boolean?
    ): List<TestCaseResponse> {
        return testCaseService.findAll(problemId, isVisible)
    }

    @GetMapping("/{testcaseId}")
    fun findById(
        @PathVariable problemId: Long,
        @PathVariable testcaseId: Long
    ): TestCaseResponse {
        return testCaseService.findById(problemId, testcaseId)
    }

    @PatchMapping("/{testcaseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable problemId: Long,
        @PathVariable testcaseId: Long,
        @Valid @RequestBody request: UpdateTestCaseRequest
    ) {
        testCaseService.update(problemId, testcaseId, request)
    }

    @DeleteMapping("/{testcaseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable problemId: Long,
        @PathVariable testcaseId: Long
    ) {
        testCaseService.delete(problemId, testcaseId)
    }
}
