package me.suhyun.soj.domain.auth.presentation

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import me.suhyun.soj.domain.auth.application.service.AuthService
import me.suhyun.soj.domain.auth.presentation.dto.MergeGuestRequest
import me.suhyun.soj.global.security.jwt.JwtAuthenticationFilter
import me.suhyun.soj.global.security.util.CookieUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/auth")
class AuthController(
    private val cookieUtils: CookieUtils,
    private val authService: AuthService
) {

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(response: HttpServletResponse) {
        cookieUtils.clearAuthCookies(response)
    }

    @PostMapping("/merge-guest")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun mergeGuest(
        @Valid @RequestBody request: MergeGuestRequest,
        @RequestAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE) userId: UUID
    ) {
        authService.mergeGuestRecords(request.guestId, userId)
    }
}
