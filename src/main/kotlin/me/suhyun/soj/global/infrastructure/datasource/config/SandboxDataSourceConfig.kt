package me.suhyun.soj.global.infrastructure.datasource.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class SandboxDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "sandbox.datasource.admin")
    fun sandboxAdminDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    @ConfigurationProperties(prefix = "sandbox.datasource.readonly")
    fun sandboxReadonlyDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}
