package me.suhyun.soj.domain.auth.application.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth")
data class OAuthProperties(
    val redirectUrl: String
)
