package me.suhyun.soj.domain.testcase.infrastructure.mongo

import org.springframework.data.mongodb.repository.MongoRepository

interface TestCaseMetadataMongoRepository : MongoRepository<TestCaseMetadataDocument, String> {
    fun findByTestCaseId(testCaseId: Long): TestCaseMetadataDocument?
    fun findByTestCaseIdIn(testCaseIds: List<Long>): List<TestCaseMetadataDocument>
    fun deleteByTestCaseId(testCaseId: Long)
    fun deleteAllByTestCaseIdIn(testCaseIds: List<Long>)
}
