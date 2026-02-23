package me.suhyun.soj.domain.run.domain.repository

import me.suhyun.soj.domain.run.domain.entity.RunEntity
import me.suhyun.soj.domain.run.domain.model.Run
import me.suhyun.soj.domain.run.domain.model.enums.RunStatus
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class RunRepositoryImpl : RunRepository {

    override fun save(run: Run): Run {
        val entity = RunEntity.new {
            this.problemId = run.problemId
            this.query = run.query
            this.status = run.status
            this.result = run.result
            this.errorMessage = run.errorMessage
            this.createdAt = run.createdAt
        }
        return RunEntity.from(entity)
    }

    override fun findById(id: Long): Run? {
        return RunEntity.findById(id)
            ?.takeIf { it.deletedAt == null }
            ?.let { RunEntity.from(it) }
    }

    override fun updateResult(id: Long, status: RunStatus, result: String?, errorMessage: String?) {
        val entity = RunEntity.findById(id) ?: return
        entity.status = status
        entity.result = result
        entity.errorMessage = errorMessage
        entity.updatedAt = LocalDateTime.now()
    }
}
