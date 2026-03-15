package me.suhyun.soj.domain.auth.presentation

import jakarta.servlet.http.HttpServletResponse
import me.suhyun.soj.domain.user.application.service.UserService
import me.suhyun.soj.domain.user.domain.model.User
import me.suhyun.soj.domain.user.domain.model.enums.AuthProvider
import me.suhyun.soj.domain.user.domain.model.enums.UserRole
import me.suhyun.soj.global.security.jwt.JwtTokenProvider
import me.suhyun.soj.global.security.util.CookieUtils
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Profile("test")
@RestController
@RequestMapping("/test")
class TestAuthController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val cookieUtils: CookieUtils
) {

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun login(@RequestParam role: UserRole, response: HttpServletResponse) {
        val (email, uuid, nickname) = when (role) {
            UserRole.USER -> Triple("test-user@test.com", UUID.fromString("00000000-0000-0000-0000-000000000001"), "testUser")
            UserRole.ADMIN -> Triple("test-admin@test.com", UUID.fromString("00000000-0000-0000-0000-000000000002"), "testAdmin")
        }

        val user = userService.findByEmail(email) ?: userService.save(
            User(
                uuid = uuid,
                email = email,
                nickname = nickname,
                provider = AuthProvider.LOCAL,
                role = role
            )
        )

        cookieUtils.addAccessTokenCookie(response, jwtTokenProvider.createAccessToken(user))
        cookieUtils.addRefreshTokenCookie(response, jwtTokenProvider.createRefreshToken(user))
    }
}
