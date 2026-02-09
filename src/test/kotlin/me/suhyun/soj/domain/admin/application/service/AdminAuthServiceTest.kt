package me.suhyun.soj.domain.admin.application.service

import me.suhyun.soj.domain.admin.exception.AdminErrorCode
import me.suhyun.soj.domain.admin.presentation.request.AdminLoginRequest
import me.suhyun.soj.domain.user.application.service.UserService
import me.suhyun.soj.domain.user.domain.model.User
import me.suhyun.soj.domain.user.domain.model.enums.AuthProvider
import me.suhyun.soj.domain.user.domain.model.enums.UserRole
import me.suhyun.soj.global.exception.BusinessException
import me.suhyun.soj.global.security.jwt.JwtTokenProvider
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AdminAuthServiceTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    private lateinit var adminAuthService: AdminAuthService

    private val request = AdminLoginRequest(email = "admin@test.com", password = "password")

    private val adminUser = User(
        id = 1L,
        uuid = UUID.randomUUID(),
        email = "admin@test.com",
        nickname = "admin",
        password = "encodedPassword",
        provider = AuthProvider.LOCAL,
        role = UserRole.ADMIN
    )

    @BeforeEach
    fun setUp() {
        adminAuthService = AdminAuthService(userService, jwtTokenProvider, passwordEncoder)
    }

    @Nested
    inner class Login {

        @Test
        fun `should login successfully with valid admin credentials`() {
            whenever(userService.findByEmail(request.email)).thenReturn(adminUser)
            whenever(passwordEncoder.matches(request.password, adminUser.password)).thenReturn(true)
            whenever(jwtTokenProvider.createAccessToken(adminUser)).thenReturn("access-token")
            whenever(jwtTokenProvider.createRefreshToken(adminUser)).thenReturn("refresh-token")

            val response = adminAuthService.login(request)

            assertThat(response.accessToken).isEqualTo("access-token")
            assertThat(response.refreshToken).isEqualTo("refresh-token")
        }

        @Test
        fun `should throw INVALID_CREDENTIALS when user not found`() {
            whenever(userService.findByEmail(request.email)).thenReturn(null)

            assertThatThrownBy { adminAuthService.login(request) }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(AdminErrorCode.INVALID_CREDENTIALS)
        }

        @Test
        fun `should throw INVALID_CREDENTIALS when user is not ADMIN`() {
            val normalUser = adminUser.copy(role = UserRole.USER)
            whenever(userService.findByEmail(request.email)).thenReturn(normalUser)

            assertThatThrownBy { adminAuthService.login(request) }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(AdminErrorCode.INVALID_CREDENTIALS)
        }

        @Test
        fun `should throw INVALID_CREDENTIALS when password is null`() {
            val noPasswordUser = adminUser.copy(password = null)
            whenever(userService.findByEmail(request.email)).thenReturn(noPasswordUser)

            assertThatThrownBy { adminAuthService.login(request) }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(AdminErrorCode.INVALID_CREDENTIALS)
        }

        @Test
        fun `should throw INVALID_CREDENTIALS when password mismatch`() {
            whenever(userService.findByEmail(request.email)).thenReturn(adminUser)
            whenever(passwordEncoder.matches(request.password, adminUser.password)).thenReturn(false)

            assertThatThrownBy { adminAuthService.login(request) }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(AdminErrorCode.INVALID_CREDENTIALS)
        }
    }
}
