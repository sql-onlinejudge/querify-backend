package me.suhyun.soj.global.exception

import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import me.suhyun.soj.domain.admin.exception.AdminErrorCode
import me.suhyun.soj.global.infrastructure.notification.Notifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.core.MethodParameter
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

@ExtendWith(MockitoExtension::class)
class GlobalExceptionHandlerTest {

    @Mock
    private lateinit var notifier: Notifier

    @Mock
    private lateinit var environment: Environment

    private lateinit var handler: GlobalExceptionHandler

    @BeforeEach
    fun setUp() {
        handler = GlobalExceptionHandler(notifier, environment)
    }

    @Nested
    inner class BusinessExceptions {

        @Test
        fun `should handle BusinessException with correct status and code`() {
            val exception = BusinessException(AdminErrorCode.INVALID_CREDENTIALS)

            val response = handler.handleBusinessException(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(response.body?.code).isEqualTo("INVALID_CREDENTIALS")
        }
    }

    @Nested
    inner class ValidationExceptions {

        @Test
        fun `should handle MethodArgumentNotValidException with 400`() {
            val bindingResult = BeanPropertyBindingResult(Any(), "target")
            bindingResult.addError(FieldError("target", "email", "must not be blank"))
            val exception = MethodArgumentNotValidException(
                MethodParameter(this::class.java.getDeclaredMethod("should handle MethodArgumentNotValidException with 400"), -1),
                bindingResult
            )

            val response = handler.handleValidationException(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(response.body?.code).isEqualTo("VALIDATION_ERROR")
            assertThat(response.body?.message).contains("email")
        }

        @Test
        fun `should handle ConstraintViolationException with 400`() {
            @Suppress("UNCHECKED_CAST")
            val violation = org.mockito.Mockito.mock(ConstraintViolation::class.java) as ConstraintViolation<*>
            whenever(violation.message).thenReturn("must be positive")
            val exception = ConstraintViolationException(setOf(violation))

            val response = handler.handleConstraintViolation(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(response.body?.code).isEqualTo("VALIDATION_ERROR")
        }

        @Test
        fun `should handle MissingServletRequestParameterException with 400`() {
            val exception = MissingServletRequestParameterException("page", "int")

            val response = handler.handleMissingParam(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(response.body?.code).isEqualTo("MISSING_PARAMETER")
        }

        @Test
        fun `should handle MethodArgumentTypeMismatchException with 400`() {
            val methodParam = MethodParameter(this::class.java.getDeclaredMethod("should handle MethodArgumentTypeMismatchException with 400"), -1)
            val exception = MethodArgumentTypeMismatchException(
                "abc", Int::class.java, "id", methodParam, IllegalArgumentException()
            )

            val response = handler.handleTypeMismatch(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(response.body?.code).isEqualTo("TYPE_MISMATCH")
        }

        @Test
        fun `should handle HttpMessageNotReadableException with 400`() {
            val exception = HttpMessageNotReadableException("Could not read", org.springframework.mock.http.MockHttpInputMessage(ByteArray(0)))

            val response = handler.handleMessageNotReadable(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(response.body?.code).isEqualTo("INVALID_REQUEST_BODY")
        }
    }

    @Nested
    inner class NotFoundExceptions {

        @Test
        fun `should handle NoHandlerFoundException with 404`() {
            val exception = NoHandlerFoundException("GET", "/unknown", HttpHeaders())

            val response = handler.handleNotFound(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
            assertThat(response.body?.code).isEqualTo("NOT_FOUND")
        }

        @Test
        fun `should handle NoResourceFoundException with 404`() {
            val exception = NoResourceFoundException(HttpMethod.GET, "/unknown")

            val response = handler.handleNoResourceFound(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
            assertThat(response.body?.code).isEqualTo("NOT_FOUND")
        }
    }

    @Nested
    inner class HttpMethodExceptions {

        @Test
        fun `should handle HttpRequestMethodNotSupportedException with 405`() {
            val exception = HttpRequestMethodNotSupportedException("PATCH")

            val response = handler.handleMethodNotSupported(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
            assertThat(response.body?.code).isEqualTo("METHOD_NOT_ALLOWED")
        }

        @Test
        fun `should handle HttpMediaTypeNotSupportedException with 415`() {
            val exception = HttpMediaTypeNotSupportedException(MediaType.TEXT_PLAIN, listOf(MediaType.APPLICATION_JSON))

            val response = handler.handleMediaTypeNotSupported(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            assertThat(response.body?.code).isEqualTo("UNSUPPORTED_MEDIA_TYPE")
        }
    }

    @Nested
    inner class SecurityExceptions {

        @Test
        fun `should handle AuthenticationException with 401`() {
            val exception = BadCredentialsException("Bad credentials")

            val response = handler.handleAuthentication(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
            assertThat(response.body?.code).isEqualTo("UNAUTHORIZED")
        }

        @Test
        fun `should handle AccessDeniedException with 403`() {
            val exception = AccessDeniedException("Access denied")

            val response = handler.handleAccessDenied(exception)

            assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
            assertThat(response.body?.code).isEqualTo("FORBIDDEN")
        }
    }

    @Nested
    inner class GenericExceptions {

        @Test
        fun `should handle generic Exception with 500 and send notification`() {
            val exception = RuntimeException("Unexpected error")
            val request = MockHttpServletRequest("GET", "/api/test")
            whenever(environment.activeProfiles).thenReturn(arrayOf("test"))

            val response = handler.handleException(exception, request)

            assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
            assertThat(response.body?.code).isEqualTo("INTERNAL_ERROR")
            verify(notifier).notify(any(), any(), any(), any(), any(), any(), any(), any())
        }
    }
}
