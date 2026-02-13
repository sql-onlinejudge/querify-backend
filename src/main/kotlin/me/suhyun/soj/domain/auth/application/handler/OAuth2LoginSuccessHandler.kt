package me.suhyun.soj.domain.auth.application.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.suhyun.soj.domain.auth.application.config.OAuthProperties
import me.suhyun.soj.domain.user.domain.model.User
import me.suhyun.soj.domain.user.domain.model.enums.AuthProvider
import me.suhyun.soj.domain.user.domain.model.enums.UserRole
import me.suhyun.soj.domain.user.domain.repository.UserRepository
import me.suhyun.soj.global.security.jwt.JwtTokenProvider
import me.suhyun.soj.global.security.util.CookieUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class OAuth2LoginSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val oAuthProperties: OAuthProperties,
    private val cookieUtils: CookieUtils
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2Token = authentication as OAuth2AuthenticationToken
        val oAuth2User = oAuth2Token.principal
        val registrationId = oAuth2Token.authorizedClientRegistrationId

        val (provider, providerId, email, nickname, profileImageUrl) = when (registrationId) {
            "google" -> OAuthUserInfo(
                provider = AuthProvider.GOOGLE,
                providerId = oAuth2User.attributes["sub"] as String,
                email = oAuth2User.attributes["email"] as? String,
                nickname = oAuth2User.attributes["name"] as? String,
                profileImageUrl = oAuth2User.attributes["picture"] as? String
            )
            "github" -> OAuthUserInfo(
                provider = AuthProvider.GITHUB,
                providerId = (oAuth2User.attributes["id"] as Int).toString(),
                email = oAuth2User.attributes["email"] as? String,
                nickname = oAuth2User.attributes["login"] as? String,
                profileImageUrl = oAuth2User.attributes["avatar_url"] as? String
            )
            else -> throw IllegalArgumentException("Unsupported OAuth2 provider: $registrationId")
        }

        val user = transaction {
            userRepository.findByProviderAndProviderId(provider, providerId)
                ?: userRepository.save(
                    User(
                        uuid = UUID.randomUUID(),
                        email = email,
                        nickname = nickname,
                        profileImageUrl = profileImageUrl,
                        provider = provider,
                        providerId = providerId,
                        role = UserRole.USER
                    )
                )
        }

        val accessToken = jwtTokenProvider.createAccessToken(user)
        val refreshToken = jwtTokenProvider.createRefreshToken(user)

        cookieUtils.addAccessTokenCookie(response, accessToken)
        cookieUtils.addRefreshTokenCookie(response, refreshToken)

        response.sendRedirect(oAuthProperties.redirectUrl)
    }

    private data class OAuthUserInfo(
        val provider: AuthProvider,
        val providerId: String,
        val email: String?,
        val nickname: String?,
        val profileImageUrl: String?
    )
}
