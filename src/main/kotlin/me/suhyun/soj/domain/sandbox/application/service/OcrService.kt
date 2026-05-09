package me.suhyun.soj.domain.sandbox.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import me.suhyun.soj.domain.sandbox.exception.SandboxErrorCode
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.Base64

@Service
class OcrService(
    @Qualifier("anthropicWebClient")
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper
) {

    fun extractSql(imageBytes: ByteArray, mediaType: String): String {
        val base64Image = Base64.getEncoder().encodeToString(imageBytes)
        val requestBody = mapOf(
            "model" to "claude-haiku-4-5-20251001",
            "max_tokens" to 2048,
            "system" to "Extract only MySQL DDL (CREATE TABLE) and INSERT INTO statements from SQL exam images. Output SQL only, no explanation.",
            "messages" to listOf(
                mapOf(
                    "role" to "user",
                    "content" to listOf(
                        mapOf(
                            "type" to "image",
                            "source" to mapOf(
                                "type" to "base64",
                                "media_type" to mediaType,
                                "data" to base64Image
                            )
                        )
                    )
                )
            )
        )

        val responseBody = webClient.post()
            .uri("/v1/messages")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: throw BusinessException(SandboxErrorCode.OCR_EXTRACTION_FAILED)

        return parseSqlFromResponse(responseBody)
    }

    private fun parseSqlFromResponse(responseBody: String): String {
        val root = objectMapper.readTree(responseBody)
        val text = root.path("content").path(0).path("text").asText("")
        if (text.isBlank()) throw BusinessException(SandboxErrorCode.OCR_EXTRACTION_FAILED)

        val codeBlockRegex = Regex("```(?:sql)?\\s*([\\s\\S]*?)```", RegexOption.IGNORE_CASE)
        val match = codeBlockRegex.find(text)
        val sql = if (match != null) match.groupValues[1].trim() else text.trim()

        if (sql.isBlank()) throw BusinessException(SandboxErrorCode.OCR_EXTRACTION_FAILED)
        return normalizeToMySql(sql)
    }

    private fun normalizeToMySql(sql: String): String = sql
        .replace(Regex("\\bNUMBER\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)", RegexOption.IGNORE_CASE)) { m ->
            "DECIMAL(${m.groupValues[1]},${m.groupValues[2]})"
        }
        .replace(Regex("\\bNUMBER\\s*\\(\\s*\\d+\\s*\\)", RegexOption.IGNORE_CASE), "INT")
        .replace(Regex("\\bNUMBER\\b", RegexOption.IGNORE_CASE), "BIGINT")
        .replace(Regex("\\bVARCHAR2\\b", RegexOption.IGNORE_CASE), "VARCHAR")
        .replace(Regex("\\bCLOB\\b", RegexOption.IGNORE_CASE), "TEXT")
        .replace(Regex("\\bBLOB\\b", RegexOption.IGNORE_CASE), "LONGBLOB")
}
