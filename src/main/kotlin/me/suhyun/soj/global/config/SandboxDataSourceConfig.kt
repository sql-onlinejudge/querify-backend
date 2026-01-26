package me.suhyun.soj.global.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class SandboxDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "sandbox.datasource")
    fun sandboxDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}
