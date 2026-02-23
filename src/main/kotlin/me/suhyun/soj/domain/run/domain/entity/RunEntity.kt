package me.suhyun.soj.domain.run.domain.entity

import me.suhyun.soj.domain.run.domain.model.Run
import me.suhyun.soj.domain.run.domain.model.enums.RunStatus
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RunEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RunEntity>(RunTable) {
        fun from(entity: RunEntity) = Run(
            id = entity.id.value,
            problemId = entity.problemId,
            query = entity.query,
            status = entity.status,
            result = entity.result,
            errorMessage = entity.errorMessage,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt
        )
    }

    var problemId by RunTable.problemId
    var query by RunTable.query
    var status by RunTable.status
    var result by RunTable.result
    var errorMessage by RunTable.errorMessage
    var createdAt by RunTable.createdAt
    var updatedAt by RunTable.updatedAt
    var deletedAt by RunTable.deletedAt
}
