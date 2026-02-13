package me.suhyun.soj.global.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import me.suhyun.soj.global.security.util.CookieUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class JwtAuthenticationFilterTest {

    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Mock
    private lateinit var cookieUtils: CookieUtils

    @Mock
    private lateinit var filterChain: FilterChain

    private lateinit var filter: JwtAuthenticationFilter

    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse

    @BeforeEach
    fun setUp() {
        filter = JwtAuthenticationFilter(jwtTokenProvider, cookieUtils)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
        SecurityContextHolder.clearContext()
    }

    @Nested
    inner class CookieTokenAuth {

        @Test
        fun `should authenticate when valid cookie token provided`() {
            val uuid = UUID.randomUUID()
            val token = "valid-token"

            whenever(cookieUtils.resolveAccessToken(request)).thenReturn(token)
            whenever(jwtTokenProvider.validateToken(token)).thenReturn(true)
            val auth = UsernamePasswordAuthenticationToken(
                uuid, null, listOf(SimpleGrantedAuthority("ROLE_USER"))
            )
            whenever(jwtTokenProvider.getAuthentication(token)).thenReturn(auth)

            filter.doFilter(request, response, filterChain)

            assertThat(SecurityContextHolder.getContext().authentication).isNotNull
            assertThat(SecurityContextHolder.getContext().authentication.principal).isEqualTo(uuid)
            verify(filterChain).doFilter(request, response)
        }

        @Test
        fun `should set userId attribute when valid token`() {
            val uuid = UUID.randomUUID()
            val token = "valid-token"

            whenever(cookieUtils.resolveAccessToken(request)).thenReturn(token)
            whenever(jwtTokenProvider.validateToken(token)).thenReturn(true)
            val auth = UsernamePasswordAuthenticationToken(
                uuid, null, listOf(SimpleGrantedAuthority("ROLE_USER"))
            )
            whenever(jwtTokenProvider.getAuthentication(token)).thenReturn(auth)

            filter.doFilter(request, response, filterChain)

            assertThat(request.getAttribute("userId")).isEqualTo(uuid)
        }
    }

    @Nested
    inner class XUserIdHeaderAuth {

        @Test
        fun `should authenticate via X-User-Id header when no token`() {
            val uuid = UUID.randomUUID()
            request.addHeader("X-User-Id", uuid.toString())

            whenever(cookieUtils.resolveAccessToken(request)).thenReturn(null)

            filter.doFilter(request, response, filterChain)

            assertThat(SecurityContextHolder.getContext().authentication).isNotNull
            assertThat(request.getAttribute("userId")).isEqualTo(uuid)
            verify(filterChain).doFilter(request, response)
        }

        @Test
        fun `should fallback to X-User-Id when token is invalid`() {
            val uuid = UUID.randomUUID()
            request.addHeader("X-User-Id", uuid.toString())

            whenever(cookieUtils.resolveAccessToken(request)).thenReturn("invalid-token")
            whenever(jwtTokenProvider.validateToken("invalid-token")).thenReturn(false)

            filter.doFilter(request, response, filterChain)

            assertThat(request.getAttribute("userId")).isEqualTo(uuid)
            verify(filterChain).doFilter(request, response)
        }

        @Test
        fun `should skip auth when X-User-Id is invalid UUID`() {
            request.addHeader("X-User-Id", "not-a-uuid")

            whenever(cookieUtils.resolveAccessToken(request)).thenReturn(null)

            filter.doFilter(request, response, filterChain)

            assertThat(SecurityContextHolder.getContext().authentication).isNull()
            verify(filterChain).doFilter(request, response)
        }
    }

    @Nested
    inner class NoAuth {

        @Test
        fun `should skip auth when no token and no X-User-Id`() {
            whenever(cookieUtils.resolveAccessToken(request)).thenReturn(null)

            filter.doFilter(request, response, filterChain)

            assertThat(SecurityContextHolder.getContext().authentication).isNull()
            verify(filterChain).doFilter(request, response)
        }
    }
}
