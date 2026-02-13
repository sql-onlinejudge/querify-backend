package me.suhyun.soj.global.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.suhyun.soj.global.security.util.CookieUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val cookieUtils: CookieUtils
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = cookieUtils.resolveAccessToken(request)

        if (token != null && jwtTokenProvider.validateToken(token)) {
            val authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
            request.setAttribute(USER_ID_ATTRIBUTE, authentication.principal as UUID)
        } else {
            val userId = request.getHeader(USER_ID_HEADER)
            if (userId != null) {
                try {
                    val uuid = UUID.fromString(userId)
                    request.setAttribute(USER_ID_ATTRIBUTE, uuid)

                    val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                    val authentication = UsernamePasswordAuthenticationToken(uuid, null, authorities)
                    SecurityContextHolder.getContext().authentication = authentication
                } catch (e: IllegalArgumentException) {
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    companion object {
        private const val USER_ID_HEADER = "X-User-Id"
        const val USER_ID_ATTRIBUTE = "userId"
    }
}
