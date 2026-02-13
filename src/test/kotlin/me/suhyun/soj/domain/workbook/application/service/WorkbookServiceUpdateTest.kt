package me.suhyun.soj.domain.workbook.application.service

import me.suhyun.soj.domain.workbook.domain.model.Workbook
import me.suhyun.soj.domain.workbook.domain.repository.WorkbookRepository
import me.suhyun.soj.domain.workbook.exception.WorkbookErrorCode
import me.suhyun.soj.domain.workbook.presentation.request.UpdateWorkbookRequest
import me.suhyun.soj.global.exception.BusinessException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class WorkbookServiceUpdateTest {

    @Mock
    private lateinit var workbookRepository: WorkbookRepository

    private lateinit var workbookService: WorkbookService

    @BeforeEach
    fun setUp() {
        workbookService = WorkbookService(workbookRepository)
    }

    @Test
    fun `should update workbook successfully`() {
        val request = UpdateWorkbookRequest(name = "수정된 이름", description = "수정된 설명", difficulty = 5)

        whenever(workbookRepository.update(1L, request.name, request.description, request.difficulty)).thenReturn(
            Workbook(1L, request.name, request.description, request.difficulty, LocalDateTime.now(), LocalDateTime.now(), null)
        )

        workbookService.update(1L, request)

        verify(workbookRepository).update(1L, request.name, request.description, request.difficulty)
    }

    @Test
    fun `should throw WORKBOOK_NOT_FOUND when updating non-existent workbook`() {
        val request = UpdateWorkbookRequest(name = "이름", description = "설명", difficulty = 1)

        whenever(workbookRepository.update(999L, request.name, request.description, request.difficulty)).thenReturn(null)

        assertThatThrownBy { workbookService.update(999L, request) }
            .isInstanceOf(BusinessException::class.java)
            .extracting("errorCode")
            .isEqualTo(WorkbookErrorCode.WORKBOOK_NOT_FOUND)
    }
}
