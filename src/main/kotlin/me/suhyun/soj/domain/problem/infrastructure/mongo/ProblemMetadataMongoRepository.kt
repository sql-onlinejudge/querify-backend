package me.suhyun.soj.domain.problem.infrastructure.mongo

import org.springframework.data.mongodb.repository.MongoRepository

interface ProblemMetadataMongoRepository : MongoRepository<ProblemMetadataDocument, String> {
    fun findByProblemId(problemId: Long): ProblemMetadataDocument?
    fun deleteByProblemId(problemId: Long)
}
