package me.suhyun.soj.domain.sandbox.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import me.suhyun.soj.domain.sandbox.exception.SandboxErrorCode
import me.suhyun.soj.global.exception.BusinessException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO

@Service
class OcrService(
    @Qualifier("anthropicWebClient")
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper
) {

    fun extractSql(imageBytes: ByteArray, mediaType: String): String {
        val (resizedBytes, resolvedMediaType) = resizeImage(imageBytes, mediaType)
        val base64Image = Base64.getEncoder().encodeToString(resizedBytes)
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
                                "media_type" to resolvedMediaType,
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

    private fun resizeImage(imageBytes: ByteArray, originalMediaType: String): Pair<ByteArray, String> {
        val src = ImageIO.read(ByteArrayInputStream(imageBytes))
            ?: return imageBytes to originalMediaType
        val maxDim = 1568
        val scale = minOf(maxDim.toDouble() / src.width, maxDim.toDouble() / src.height, 1.0)
        if (scale >= 1.0) return imageBytes to originalMediaType
        val w = (src.width * scale).toInt()
        val h = (src.height * scale).toInt()
        val dst = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
        val g = dst.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        g.drawImage(src, 0, 0, w, h, null)
        g.dispose()
        val format = if (originalMediaType == "image/jpeg") "jpeg" else "png"
        val resolvedType = if (originalMediaType == "image/jpeg") "image/jpeg" else "image/png"
        val out = ByteArrayOutputStream()
        ImageIO.write(dst, format, out)
        return out.toByteArray() to resolvedType
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
