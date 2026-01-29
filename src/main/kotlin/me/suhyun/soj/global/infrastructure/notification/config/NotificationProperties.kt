package me.suhyun.soj.global.infrastructure.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "notification")
data class NotificationProperties(
    val discord: DiscordProperties = DiscordProperties(),
) {
    data class DiscordProperties(
        val enabled: Boolean = false,
        val webhookUrl: String = ""
    )
}
