package me.suhyun.soj.domain.submission.application.scheduler

import me.suhyun.soj.domain.problem.domain.entity.ProblemTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.update
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class SubmissionScheduler(
    private val redisTemplate: StringRedisTemplate
) {
    @Scheduled(fixedRate = 60000)
    @Transactional
    fun syncSubmissionCounts() {
        val keys = redisTemplate.keys("problem:submissionCount:*") ?: return

        keys.forEach { key ->
            val count = redisTemplate.opsForValue().get(key)?.toLongOrNull() ?: return@forEach
            val problemId = key.substringAfterLast(":").toLongOrNull() ?: return@forEach

            ProblemTable.update({ ProblemTable.id eq problemId }) {
                it[submissionCount] = submissionCount + count.toInt()
                it[updatedAt] = LocalDateTime.now()
            }

            redisTemplate.opsForValue().decrement(key, count)
        }
    }
}
