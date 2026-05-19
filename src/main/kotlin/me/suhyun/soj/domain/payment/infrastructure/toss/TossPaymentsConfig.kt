package me.suhyun.soj.domain.payment.infrastructure.toss

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TossPaymentsProperties::class)
class TossPaymentsConfig
