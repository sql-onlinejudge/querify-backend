package me.suhyun.soj.domain.workbook.application.service

import me.suhyun.soj.domain.workbook.domain.repository.WorkbookRepository
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
class WorkbookServiceDeleteTest {

    @Mock
    private lateinit var workbookRepository: WorkbookRepository

    private lateinit var workbookService: WorkbookService

    @BeforeEach
    fun setUp() {
        workbookService = WorkbookService(workbookRepository)
    }

    @Test
    fun `should soft delete workbook successfully`() {
        whenever(workbookRepository.softDelete(1L)).thenReturn(true)

        workbookService.delete(1L)

        verify(workbookRepository).softDelete(1L)
    }

    @Test
    fun `should throw WORKBOOK_NOT_FOUND when workbook does not exist`() {
        whenever(workbookRepository.softDelete(999L)).thenReturn(false)

        assertThatThrownBy { workbookService.delete(999L) }
            .isInstanceOf(BusinessException::class.java)
            .extracting("errorCode")
            .isEqualTo(WorkbookErrorCode.WORKBOOK_NOT_FOUND)
    }
}
