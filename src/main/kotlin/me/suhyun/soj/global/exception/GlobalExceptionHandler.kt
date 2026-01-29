package me.suhyun.soj.global.exception

import jakarta.servlet.http.HttpServletRequest
import me.suhyun.soj.global.infrastructure.notification.Notifier
import me.suhyun.soj.global.infrastructure.notification.model.enums.NotificationType
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestControllerAdvice
class GlobalExceptionHandler(
    private val notifier: Notifier,
    private val environment: Environment
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(e.errorCode.status)
            .body(ErrorResponse.of(e.errorCode))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = e.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity
            .badRequest()
            .body(ErrorResponse(code = "VALIDATION_ERROR", message = message))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        log.error("Unhandled exception", e)

        val profile = environment.activeProfiles.firstOrNull() ?: "default"
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val method = request.method
        val path = request.requestURI
        val errorType = e.javaClass.simpleName
        val message = e.message ?: "Unknown error"
        val stackTrace = e.stackTrace.take(10).joinToString("\n") { it.toString() }

        notifier.notify(NotificationType.ERROR, profile, timestamp, method, path, errorType, message, stackTrace)

        return ResponseEntity
            .internalServerError()
            .body(ErrorResponse(code = "INTERNAL_ERROR", message = "서버 오류"))
    }
}
