package me.suhyun.soj.domain.submission.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import me.suhyun.soj.domain.problem.domain.repository.ProblemRepository
import me.suhyun.soj.domain.submission.domain.repository.SubmissionRepository
import me.suhyun.soj.domain.submission.exception.FeedbackErrorCode
import me.suhyun.soj.domain.submission.exception.SubmissionErrorCode
import me.suhyun.soj.domain.submission.presentation.response.AiFeedbackResponse
import me.suhyun.soj.domain.subscription.application.service.SubscriptionService
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.util.UUID

@Service
@Transactional(readOnly = true)
class AiFeedbackService(
    @Qualifier("anthropicWebClient") private val webClient: WebClient,
    private val submissionRepository: SubmissionRepository,
    private val problemRepository: ProblemRepository,
    private val subscriptionService: SubscriptionService,
    private val objectMapper: ObjectMapper
) {

    fun generateFeedback(problemId: Long, submissionId: Long, userId: UUID): AiFeedbackResponse {
        if (!subscriptionService.isActive(userId)) {
            throw BusinessException(FeedbackErrorCode.PREMIUM_REQUIRED)
        }

        val submission = submissionRepository.findById(submissionId)
            ?: throw BusinessException(SubmissionErrorCode.SUBMISSION_NOT_FOUND)

        if (submission.problemId != problemId || submission.userId != userId) {
            throw BusinessException(SubmissionErrorCode.SUBMISSION_NOT_FOUND)
        }

        val problem = problemRepository.findById(problemId)
            ?: throw BusinessException(SubmissionErrorCode.SUBMISSION_NOT_FOUND)

        val prompt = buildPrompt(
            problemTitle = problem.title,
            problemDescription = problem.description,
            schemaSql = problem.schemaSql,
            userQuery = submission.query,
            verdict = submission.verdict?.name ?: "UNKNOWN"
        )

        return callClaude(prompt)
    }

    private fun buildPrompt(
        problemTitle: String,
        problemDescription: String,
        schemaSql: String,
        userQuery: String,
        verdict: String
    ): String = """
        문제: $problemTitle

        문제 설명:
        $problemDescription

        스키마:
        $schemaSql

        제출한 SQL:
        $userQuery

        채점 결과: $verdict

        위 정보를 바탕으로 다음 JSON 형식으로만 응답해줘 (다른 텍스트 없이):
        {
          "hint": "문제를 풀기 위한 핵심 힌트 (1-2문장)",
          "improvement": "제출한 쿼리의 구체적인 개선 방향",
          "explanation": "정답 접근법에 대한 설명"
        }
    """.trimIndent()

    private fun callClaude(prompt: String): AiFeedbackResponse {
        val requestBody = mapOf(
            "model" to "claude-haiku-4-5-20251001",
            "max_tokens" to 1024,
            "messages" to listOf(
                mapOf("role" to "user", "content" to prompt)
            )
        )

        val responseBody = webClient.post()
            .uri("/v1/messages")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: throw BusinessException(FeedbackErrorCode.FEEDBACK_GENERATION_FAILED)

        return parseResponse(responseBody)
    }

    private fun parseResponse(responseBody: String): AiFeedbackResponse {
        val root = objectMapper.readTree(responseBody)
        val text = root.path("content").path(0).path("text").asText("")

        if (text.isBlank()) throw BusinessException(FeedbackErrorCode.FEEDBACK_GENERATION_FAILED)

        val jsonRegex = Regex("\\{[\\s\\S]*\\}", RegexOption.DOT_MATCHES_ALL)
        val jsonText = jsonRegex.find(text)?.value
            ?: throw BusinessException(FeedbackErrorCode.FEEDBACK_GENERATION_FAILED)

        val parsed = objectMapper.readTree(jsonText)
        return AiFeedbackResponse(
            hint = parsed.path("hint").asText(""),
            improvement = parsed.path("improvement").asText(""),
            explanation = parsed.path("explanation").asText("")
        )
    }
}
