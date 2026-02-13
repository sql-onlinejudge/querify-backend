package me.suhyun.soj.global.security.util

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.suhyun.soj.global.security.jwt.JwtProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class CookieUtils(
    private val jwtProperties: JwtProperties
) {

    fun addAccessTokenCookie(response: HttpServletResponse, token: String) {
        addCookie(response, ACCESS_TOKEN_NAME, token, jwtProperties.accessTokenValidity / 1000)
    }

    fun addRefreshTokenCookie(response: HttpServletResponse, token: String) {
        addCookie(response, REFRESH_TOKEN_NAME, token, jwtProperties.refreshTokenValidity / 1000)
    }

    fun clearAuthCookies(response: HttpServletResponse) {
        addCookie(response, ACCESS_TOKEN_NAME, "", 0)
        addCookie(response, REFRESH_TOKEN_NAME, "", 0)
    }

    fun resolveAccessToken(request: HttpServletRequest): String? {
        return request.cookies?.firstOrNull { it.name == ACCESS_TOKEN_NAME }?.value
    }

    private fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Long) {
        val cookie = ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(maxAge)
            .sameSite("Lax")
            .build()
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
    }

    companion object {
        const val ACCESS_TOKEN_NAME = "access_token"
        const val REFRESH_TOKEN_NAME = "refresh_token"
    }
}
