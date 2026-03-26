package me.suhyun.soj.global.infrastructure.migration

import com.fasterxml.jackson.databind.ObjectMapper
import me.suhyun.soj.domain.problem.domain.model.SchemaMetadata
import me.suhyun.soj.domain.testcase.domain.model.AnswerMetadata
import me.suhyun.soj.domain.testcase.domain.model.InitMetadata
import org.bson.Document
import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class MetadataMigrationConfig(
    private val mongoTemplate: MongoTemplate,
    private val objectMapper: ObjectMapper
) {
    @Bean
    fun flywayMigrationStrategy(): FlywayMigrationStrategy = FlywayMigrationStrategy { flyway: Flyway ->
        migrateMetadataToMongo(JdbcTemplate(flyway.configuration.dataSource))
        flyway.migrate()
    }

    private fun migrateMetadataToMongo(jdbcTemplate: JdbcTemplate) {
        if (mongoTemplate.getCollection("problem_metadata").countDocuments() > 0) return

        jdbcTemplate.queryForList(
            "SELECT id, schema_metadata FROM problems WHERE schema_metadata IS NOT NULL AND deleted_at IS NULL"
        ).forEach { row ->
            val schemaMetadata = objectMapper.readValue(row["schema_metadata"] as String, SchemaMetadata::class.java)
            val doc = Document()
                .append("problemId", (row["id"] as Number).toLong())
                .append("schemaMetadata", objectMapper.convertValue(schemaMetadata, Map::class.java))
            mongoTemplate.insert(doc, "problem_metadata")
        }

        jdbcTemplate.queryForList(
            "SELECT id, init_metadata, answer_metadata FROM test_cases WHERE (init_metadata IS NOT NULL OR answer_metadata IS NOT NULL) AND deleted_at IS NULL"
        ).forEach { row ->
            val doc = Document().append("testCaseId", (row["id"] as Number).toLong())
            (row["init_metadata"] as? String)?.let {
                val initMetadata = objectMapper.readValue(it, InitMetadata::class.java)
                doc.append("initMetadata", objectMapper.convertValue(initMetadata, Map::class.java))
            }
            (row["answer_metadata"] as? String)?.let {
                val answerMetadata = objectMapper.readValue(it, AnswerMetadata::class.java)
                doc.append("answerMetadata", objectMapper.convertValue(answerMetadata, Map::class.java))
            }
            mongoTemplate.insert(doc, "testcase_metadata")
        }
    }
}
