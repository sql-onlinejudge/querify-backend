package me.suhyun.soj.global.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import me.suhyun.soj.domain.user.domain.model.User
import me.suhyun.soj.domain.user.domain.model.enums.AuthProvider
import me.suhyun.soj.domain.user.domain.model.enums.UserRole
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Date
import java.util.UUID

class JwtTokenProviderTest {

    private lateinit var jwtTokenProvider: JwtTokenProvider

    private val secret = "test-secret-key-for-testing-purposes-only-minimum-32-chars"
    private val accessTokenValidity = 3600000L
    private val refreshTokenValidity = 604800000L

    private val testUser = User(
        id = 1L,
        uuid = UUID.randomUUID(),
        email = "test@test.com",
        nickname = "tester",
        password = "password",
        provider = AuthProvider.LOCAL,
        role = UserRole.USER
    )

    @BeforeEach
    fun setUp() {
        val properties = JwtProperties(secret, accessTokenValidity, refreshTokenValidity)
        jwtTokenProvider = JwtTokenProvider(properties)
    }

    @Nested
    inner class CreateToken {

        @Test
        fun `should create access token with user uuid and role`() {
            val token = jwtTokenProvider.createAccessToken(testUser)
            val key = Keys.hmacShaKeyFor(secret.toByteArray())
            val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload

            assertThat(claims.subject).isEqualTo(testUser.uuid.toString())
            assertThat(claims["role"]).isEqualTo("USER")
        }

        @Test
        fun `should create refresh token with longer expiration`() {
            val accessToken = jwtTokenProvider.createAccessToken(testUser)
            val refreshToken = jwtTokenProvider.createRefreshToken(testUser)
            val key = Keys.hmacShaKeyFor(secret.toByteArray())

            val accessClaims = Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).payload
            val refreshClaims = Jwts.parser().verifyWith(key).build().parseSignedClaims(refreshToken).payload

            assertThat(refreshClaims.expiration).isAfter(accessClaims.expiration)
        }
    }

    @Nested
    inner class ValidateToken {

        @Test
        fun `should validate token successfully when valid`() {
            val token = jwtTokenProvider.createAccessToken(testUser)

            assertThat(jwtTokenProvider.validateToken(token)).isTrue()
        }

        @Test
        fun `should return false when token is expired`() {
            val properties = JwtProperties(secret, -1000L, -1000L)
            val expiredProvider = JwtTokenProvider(properties)
            val token = expiredProvider.createAccessToken(testUser)

            assertThat(jwtTokenProvider.validateToken(token)).isFalse()
        }

        @Test
        fun `should return false when token is malformed`() {
            assertThat(jwtTokenProvider.validateToken("invalid.token.here")).isFalse()
        }

        @Test
        fun `should return false when token has wrong signature`() {
            val otherSecret = "other-secret-key-for-testing-purposes-only-minimum-32-chars"
            val otherProperties = JwtProperties(otherSecret, accessTokenValidity, refreshTokenValidity)
            val otherProvider = JwtTokenProvider(otherProperties)
            val token = otherProvider.createAccessToken(testUser)

            assertThat(jwtTokenProvider.validateToken(token)).isFalse()
        }

        @Test
        fun `should return false when token is empty string`() {
            assertThat(jwtTokenProvider.validateToken("")).isFalse()
        }
    }

    @Nested
    inner class GetAuthentication {

        @Test
        fun `should extract authentication with correct uuid and role`() {
            val token = jwtTokenProvider.createAccessToken(testUser)

            val authentication = jwtTokenProvider.getAuthentication(token)

            assertThat(authentication.principal).isEqualTo(testUser.uuid)
            assertThat(authentication.authorities).anyMatch { it.authority == "ROLE_USER" }
        }

        @Test
        fun `should extract uuid from token`() {
            val token = jwtTokenProvider.createAccessToken(testUser)

            val uuid = jwtTokenProvider.getUuid(token)

            assertThat(uuid).isEqualTo(testUser.uuid)
        }

        @Test
        fun `should extract ADMIN role from token`() {
            val adminUser = testUser.copy(role = UserRole.ADMIN)
            val token = jwtTokenProvider.createAccessToken(adminUser)

            val authentication = jwtTokenProvider.getAuthentication(token)

            assertThat(authentication.authorities).anyMatch { it.authority == "ROLE_ADMIN" }
        }
    }
}
