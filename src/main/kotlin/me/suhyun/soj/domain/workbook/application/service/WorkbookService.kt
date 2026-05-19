package me.suhyun.soj.domain.workbook.application.service

import me.suhyun.soj.domain.subscription.application.service.SubscriptionService
import me.suhyun.soj.domain.workbook.domain.model.Workbook
import me.suhyun.soj.domain.workbook.domain.repository.WorkbookRepository
import me.suhyun.soj.domain.workbook.exception.WorkbookErrorCode
import me.suhyun.soj.domain.workbook.presentation.request.CreateWorkbookRequest
import me.suhyun.soj.domain.workbook.presentation.request.UpdateWorkbookRequest
import me.suhyun.soj.domain.workbook.presentation.response.WorkbookResponse
import me.suhyun.soj.global.common.dto.PageResponse
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class WorkbookService(
    private val workbookRepository: WorkbookRepository,
    private val subscriptionService: SubscriptionService
) {

    fun create(request: CreateWorkbookRequest) {
        workbookRepository.save(
            Workbook(
                id = null,
                name = request.name,
                description = request.description,
                difficulty = request.difficulty,
                isPremium = request.isPremium,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null
            )
        )
    }

    @Transactional(readOnly = true)
    fun findAll(
        page: Int,
        size: Int,
        minDifficulty: Int?,
        maxDifficulty: Int?,
        keyword: String?,
        sort: List<String>
    ): PageResponse<WorkbookResponse> {
        val workbooks = workbookRepository.findAll(page, size, minDifficulty, maxDifficulty, keyword, sort)
        val totalElements = workbookRepository.countAll(minDifficulty, maxDifficulty, keyword)
        return PageResponse.of(
            content = workbooks.map { WorkbookResponse.from(it) },
            page = page,
            size = size,
            totalElements = totalElements
        )
    }

    @Transactional(readOnly = true)
    fun findById(workbookId: Long): WorkbookResponse {
        val workbook = workbookRepository.findById(workbookId)
            ?: throw BusinessException(WorkbookErrorCode.WORKBOOK_NOT_FOUND)

        if (workbook.isPremium) {
            val userId = SecurityContextHolder.getContext().authentication?.principal as? UUID
            if (userId == null || !subscriptionService.isActive(userId)) {
                throw BusinessException(WorkbookErrorCode.PREMIUM_REQUIRED)
            }
        }

        return WorkbookResponse.from(workbook)
    }

    fun update(workbookId: Long, request: UpdateWorkbookRequest) {
        workbookRepository.update(
            id = workbookId,
            name = request.name,
            description = request.description,
            difficulty = request.difficulty,
            isPremium = request.isPremium
        ) ?: throw BusinessException(WorkbookErrorCode.WORKBOOK_NOT_FOUND)
    }

    fun delete(workbookId: Long) {
        val deleted = workbookRepository.softDelete(workbookId)
        if (!deleted) {
            throw BusinessException(WorkbookErrorCode.WORKBOOK_NOT_FOUND)
        }
    }
}
