package me.suhyun.soj.domain.auth.application.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.suhyun.soj.domain.auth.application.config.OAuthProperties
import me.suhyun.soj.domain.user.domain.model.enums.AuthProvider
import me.suhyun.soj.domain.user.domain.repository.UserRepository
import me.suhyun.soj.global.security.jwt.JwtTokenProvider
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2LoginSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val oAuthProperties: OAuthProperties
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as OAuth2User
        val providerId = oAuth2User.attributes["sub"] as String

        val user = transaction {
            userRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, providerId)
        } ?: return

        val accessToken = jwtTokenProvider.createAccessToken(user)
        val refreshToken = jwtTokenProvider.createRefreshToken(user)

        val redirectUrl = UriComponentsBuilder.fromUriString(oAuthProperties.redirectUrl)
            .queryParam("accessToken", accessToken)
            .queryParam("refreshToken", refreshToken)
            .build()
            .toUriString()

        response.sendRedirect(redirectUrl)
    }
}
