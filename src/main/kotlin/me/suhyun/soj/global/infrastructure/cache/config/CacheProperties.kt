package me.suhyun.soj.global.infrastructure.cache.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "cache")
data class CacheProperties(
    val provider: String = "redis",
    val ttl: TtlConfig = TtlConfig()
) {
    data class TtlConfig(
        val default: Duration = Duration.ofMinutes(10),
        val user: Duration = Duration.ofMinutes(60),
        val problem: Duration = Duration.ofMinutes(30),
        val testcase: Duration = Duration.ofMinutes(30)
    )
}
