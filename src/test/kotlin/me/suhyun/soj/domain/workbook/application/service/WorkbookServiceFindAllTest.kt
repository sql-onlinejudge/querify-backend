package me.suhyun.soj.domain.workbook.application.service

import me.suhyun.soj.domain.workbook.domain.model.Workbook
import me.suhyun.soj.domain.workbook.domain.repository.WorkbookRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class WorkbookServiceFindAllTest {

    @Mock
    private lateinit var workbookRepository: WorkbookRepository

    private lateinit var workbookService: WorkbookService

    @BeforeEach
    fun setUp() {
        workbookService = WorkbookService(workbookRepository)
    }

    @Test
    fun `should return paginated workbook list`() {
        val workbooks = listOf(
            Workbook(1L, "문제집1", "설명1", 1, LocalDateTime.now(), null, null),
            Workbook(2L, "문제집2", "설명2", 2, LocalDateTime.now(), null, null),
        )

        whenever(workbookRepository.findAll(0, 20, null, null, null, listOf("id:desc"))).thenReturn(workbooks)
        whenever(workbookRepository.countAll(null, null, null)).thenReturn(2L)

        val result = workbookService.findAll(0, 20, null, null, null, listOf("id:desc"))

        assertThat(result.content).hasSize(2)
        assertThat(result.totalElements).isEqualTo(2L)
        assertThat(result.page).isEqualTo(0)
        assertThat(result.content[0].name).isEqualTo("문제집1")
    }

    @Test
    fun `should return empty page when no workbooks`() {
        whenever(workbookRepository.findAll(0, 20, null, null, null, listOf("id:desc"))).thenReturn(emptyList())
        whenever(workbookRepository.countAll(null, null, null)).thenReturn(0L)

        val result = workbookService.findAll(0, 20, null, null, null, listOf("id:desc"))

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0L)
    }
}
