package me.suhyun.soj.domain.problem.application.service

import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.problem.exception.ProblemErrorCode
import me.suhyun.soj.domain.submission.domain.repository.SubmissionRepository
import me.suhyun.soj.domain.testcase.domain.repository.TestCaseRepository
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
class ProblemServiceDeleteTest {

    @Mock
    private lateinit var problemRepository: ProblemRepository

    @Mock
    private lateinit var testCaseRepository: TestCaseRepository

    @Mock
    private lateinit var submissionRepository: SubmissionRepository

    private lateinit var problemService: ProblemService

    @BeforeEach
    fun setUp() {
        problemService = ProblemService(problemRepository, testCaseRepository, submissionRepository)
    }

    @Test
    fun `should soft delete problem successfully`() {
        whenever(problemRepository.softDelete(1L)).thenReturn(true)

        problemService.delete(1L)

        verify(problemRepository).softDelete(1L)
    }

    @Test
    fun `should throw PROBLEM_NOT_FOUND when problem does not exist`() {
        whenever(problemRepository.softDelete(999L)).thenReturn(false)

        assertThatThrownBy { problemService.delete(999L) }
            .isInstanceOf(BusinessException::class.java)
            .extracting("errorCode")
            .isEqualTo(ProblemErrorCode.PROBLEM_NOT_FOUND)
    }
}
