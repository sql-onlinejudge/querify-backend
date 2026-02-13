package me.suhyun.soj.domain.workbook.application.service

import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.workbook.domain.model.WorkbookProblem
import me.suhyun.soj.domain.workbook.domain.repository.WorkbookProblemRepository
import me.suhyun.soj.domain.workbook.exception.WorkbookErrorCode
import me.suhyun.soj.global.exception.BusinessException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class WorkbookProblemServiceDeleteTest {

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
    fun `should delete workbook problem successfully`() {
        val workbookProblem = WorkbookProblem(id = 1L, workbookId = 1L, problemId = 10L)

        whenever(workbookProblemRepository.findByWorkbookIdAndProblemId(1L, 10L)).thenReturn(workbookProblem)
        whenever(workbookProblemRepository.delete(1L)).thenReturn(true)

        workbookProblemService.delete(1L, 10L)

        verify(workbookProblemRepository).delete(1L)
    }

    @Test
    fun `should throw WORKBOOK_PROBLEM_NOT_FOUND when not found`() {
        whenever(workbookProblemRepository.findByWorkbookIdAndProblemId(1L, 999L)).thenReturn(null)

        assertThatThrownBy { workbookProblemService.delete(1L, 999L) }
            .isInstanceOf(BusinessException::class.java)
            .extracting("errorCode")
            .isEqualTo(WorkbookErrorCode.WORKBOOK_PROBLEM_NOT_FOUND)
    }

    @Test
    fun `should throw WORKBOOK_PROBLEM_NOT_FOUND when delete fails`() {
        val workbookProblem = WorkbookProblem(id = 1L, workbookId = 1L, problemId = 10L)

        whenever(workbookProblemRepository.findByWorkbookIdAndProblemId(1L, 10L)).thenReturn(workbookProblem)
        whenever(workbookProblemRepository.delete(1L)).thenReturn(false)

        assertThatThrownBy { workbookProblemService.delete(1L, 10L) }
            .isInstanceOf(BusinessException::class.java)
            .extracting("errorCode")
            .isEqualTo(WorkbookErrorCode.WORKBOOK_PROBLEM_NOT_FOUND)
    }
}
