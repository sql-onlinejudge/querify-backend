package me.suhyun.soj.domain.submission.presentation

import me.suhyun.soj.domain.submission.application.service.AiFeedbackService
import me.suhyun.soj.domain.submission.presentation.response.AiFeedbackResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/problems/{problemId}/submissions/{submissionId}/feedback")
class FeedbackController(
    private val aiFeedbackService: AiFeedbackService
) {

    @PostMapping
    fun generateFeedback(
        @PathVariable problemId: Long,
        @PathVariable submissionId: Long
    ): AiFeedbackResponse {
        val userId = SecurityContextHolder.getContext().authentication.principal as UUID
        return aiFeedbackService.generateFeedback(problemId, submissionId, userId)
    }
}
