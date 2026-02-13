package me.suhyun.soj.domain.workbook.application.service

import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.workbook.domain.model.WorkbookProblem
import me.suhyun.soj.domain.workbook.domain.repository.WorkbookProblemRepository
import me.suhyun.soj.domain.workbook.exception.WorkbookErrorCode
import me.suhyun.soj.domain.workbook.presentation.request.CreateWorkbookProblemRequest
import me.suhyun.soj.global.exception.BusinessException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class WorkbookProblemServiceCreateTest {

    @Mock
    private lateinit var workbookProblemRepository: WorkbookProblemRepository

    @Mock
    private lateinit var problemRepository: ProblemRepository

    private lateinit var workbookProblemService: WorkbookProblemService

    @BeforeEach
    fun setUp() {
        workbookProblemService = WorkbookProblemService(workbookProblemRepository, problemRepository)
    }

    @Test
    fun `should add problem to workbook successfully`() {
        val request = CreateWorkbookProblemRequest(problemId = 10L)

        whenever(workbookProblemRepository.findByWorkbookIdAndProblemId(1L, 10L)).thenReturn(null)
        whenever(workbookProblemRepository.save(any())).thenReturn(
            WorkbookProblem(id = 1L, workbookId = 1L, problemId = 10L)
        )

        workbookProblemService.create(1L, request)

        verify(workbookProblemRepository).save(any())
    }

    @Test
    fun `should throw WORKBOOK_PROBLEM_ALREADY_EXISTS when duplicate`() {
        val request = CreateWorkbookProblemRequest(problemId = 10L)
        val existing = WorkbookProblem(id = 1L, workbookId = 1L, problemId = 10L)

        whenever(workbookProblemRepository.findByWorkbookIdAndProblemId(1L, 10L)).thenReturn(existing)

        assertThatThrownBy { workbookProblemService.create(1L, request) }
            .isInstanceOf(BusinessException::class.java)
            .extracting("errorCode")
            .isEqualTo(WorkbookErrorCode.WORKBOOK_PROBLEM_ALREADY_EXISTS)
    }
}
