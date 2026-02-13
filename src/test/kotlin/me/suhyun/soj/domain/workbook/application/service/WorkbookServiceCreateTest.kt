package me.suhyun.soj.domain.workbook.application.service

import me.suhyun.soj.domain.workbook.domain.model.Workbook
import me.suhyun.soj.domain.workbook.domain.repository.WorkbookRepository
import me.suhyun.soj.domain.workbook.presentation.request.CreateWorkbookRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class WorkbookServiceCreateTest {

    @Mock
    private lateinit var workbookRepository: WorkbookRepository

    private lateinit var workbookService: WorkbookService

    @BeforeEach
    fun setUp() {
        workbookService = WorkbookService(workbookRepository)
    }

    @Test
    fun `should create workbook successfully`() {
        val request = CreateWorkbookRequest(
            name = "SQL 기초",
            description = "SQL 기초 문제집",
            difficulty = 1
        )

        whenever(workbookRepository.save(any())).thenReturn(
            Workbook(
                id = 1L,
                name = request.name,
                description = request.description,
                difficulty = request.difficulty,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null
            )
        )

        workbookService.create(request)

        verify(workbookRepository).save(any())
    }
}
