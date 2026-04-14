package me.suhyun.soj.domain.problem.infrastructure.mongo

import me.suhyun.soj.domain.problem.domain.model.OrmMetadata
import me.suhyun.soj.domain.problem.domain.model.SchemaMetadata
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "problem_metadata")
data class ProblemMetadataDocument(
    @Id val id: String? = null,
    @Indexed(unique = true) val problemId: Long,
    val schemaMetadata: SchemaMetadata,
    val ormMetadata: OrmMetadata? = null
)
