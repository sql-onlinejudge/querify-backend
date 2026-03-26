package me.suhyun.soj.domain.testcase.infrastructure.mongo

import me.suhyun.soj.domain.testcase.domain.model.AnswerMetadata
import me.suhyun.soj.domain.testcase.domain.model.InitMetadata
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "testcase_metadata")
data class TestCaseMetadataDocument(
    @Id val id: String? = null,
    @Indexed(unique = true) val testCaseId: Long,
    val initMetadata: InitMetadata?,
    val answerMetadata: AnswerMetadata?
)
