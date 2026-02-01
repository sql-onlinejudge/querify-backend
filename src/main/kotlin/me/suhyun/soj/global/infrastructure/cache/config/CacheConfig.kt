package me.suhyun.soj.global.infrastructure.cache.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
@Configuration
@EnableCaching
class CacheConfig(
    private val properties: CacheProperties,
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        val jsonSerializer = GenericJackson2JsonRedisSerializer(objectMapper)

        val defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(properties.ttl.default)
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer)
            )

        val cacheConfigs = mapOf(
            "users" to defaultConfig.entryTtl(properties.ttl.user),
            "problems" to defaultConfig.entryTtl(properties.ttl.problem),
            "testcases" to defaultConfig.entryTtl(properties.ttl.testcase)
        )

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigs)
            .build()
    }
}
