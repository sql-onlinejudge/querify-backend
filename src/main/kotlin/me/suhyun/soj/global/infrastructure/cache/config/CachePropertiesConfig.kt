package me.suhyun.soj.global.infrastructure.cache.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CacheProperties::class)
class CachePropertiesConfig
