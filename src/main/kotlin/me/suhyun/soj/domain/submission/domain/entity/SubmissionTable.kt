package me.suhyun.soj.domain.submission.domain.entity

import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.model.enums.SubmissionVerdict
import me.suhyun.soj.global.common.entity.BaseTable

@Suppress("MagicNumber")
object SubmissionTable : BaseTable("submissions") {
    val problemId = long("problem_id")
    val userId = varchar("user_id", 36)
    val query = text("query")
    val status = enumerationByName<SubmissionStatus>("status", 20)
    val verdict = enumerationByName<SubmissionVerdict>("verdict", 30).nullable()
}
