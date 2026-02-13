package me.suhyun.soj.domain.admin.presentation

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import me.suhyun.soj.domain.admin.application.service.AdminAuthService
import me.suhyun.soj.domain.admin.presentation.request.AdminLoginRequest
import me.suhyun.soj.global.security.util.CookieUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminAuthController(
    private val adminAuthService: AdminAuthService,
    private val cookieUtils: CookieUtils
) {

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun login(@Valid @RequestBody request: AdminLoginRequest, response: HttpServletResponse) {
        val (accessToken, refreshToken) = adminAuthService.login(request)
        cookieUtils.addAccessTokenCookie(response, accessToken)
        cookieUtils.addRefreshTokenCookie(response, refreshToken)
    }
}
