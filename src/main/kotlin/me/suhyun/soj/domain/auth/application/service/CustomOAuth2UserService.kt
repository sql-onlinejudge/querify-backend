package me.suhyun.soj.domain.auth.application.service

import me.suhyun.soj.domain.user.domain.model.User
import me.suhyun.soj.domain.user.domain.model.enums.AuthProvider
import me.suhyun.soj.domain.user.domain.model.enums.UserRole
import me.suhyun.soj.domain.user.domain.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val attributes = oAuth2User.attributes

        val providerId = attributes["sub"] as String
        val email = attributes["email"] as? String
        val name = attributes["name"] as? String
        val picture = attributes["picture"] as? String

        transaction {
            val existing = userRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, providerId)
            if (existing == null) {
                userRepository.save(
                    User(
                        uuid = UUID.randomUUID(),
                        email = email,
                        nickname = name,
                        profileImageUrl = picture,
                        provider = AuthProvider.GOOGLE,
                        providerId = providerId,
                        role = UserRole.USER
                    )
                )
            }
        }

        return oAuth2User
    }
}
