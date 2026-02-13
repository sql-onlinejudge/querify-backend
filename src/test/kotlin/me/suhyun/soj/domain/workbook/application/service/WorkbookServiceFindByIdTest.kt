package me.suhyun.soj.domain.workbook.application.service

import me.suhyun.soj.domain.workbook.domain.model.Workbook
import me.suhyun.soj.domain.workbook.domain.repository.WorkbookRepository
import me.suhyun.soj.domain.workbook.exception.WorkbookErrorCode
import me.suhyun.soj.global.exception.BusinessException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class WorkbookServiceFindByIdTest {

    @Mock
    private lateinit var workbookRepository: WorkbookRepository

    private lateinit var workbookService: WorkbookService

    @BeforeEach
    fun setUp() {
        workbookService = WorkbookService(workbookRepository)
    }

    @Test
    fun `should return workbook response when found`() {
        val workbook = Workbook(
            id = 1L,
            name = "SQL 기초",
            description = "SQL 기초 문제집",
            difficulty = 1,
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            deletedAt = null
        )

        whenever(workbookRepository.findById(1L)).thenReturn(workbook)

        val result = workbookService.findById(1L)

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("SQL 기초")
        assertThat(result.difficulty).isEqualTo(1L)
    }

    @Test
    fun `should throw WORKBOOK_NOT_FOUND when not found`() {
        whenever(workbookRepository.findById(999L)).thenReturn(null)

        assertThatThrownBy { workbookService.findById(999L) }
            .isInstanceOf(BusinessException::class.java)
            .extracting("errorCode")
            .isEqualTo(WorkbookErrorCode.WORKBOOK_NOT_FOUND)
    }
}
