package me.suhyun.soj.domain.submission.domain

import me.suhyun.soj.domain.problem.domain.entity.ProblemTable
import me.suhyun.soj.domain.submission.domain.enums.SubmissionStatus
import me.suhyun.soj.domain.submission.domain.enums.SubmissionVerdict
import me.suhyun.soj.global.entity.BaseTable

@Suppress("MagicNumber")
object SubmissionTable : BaseTable("submissions") {
    val problemId = reference("problem_id", ProblemTable)
    val userId = varchar("user_id", 36)
    val query = text("query")
    val status = enumerationByName<SubmissionStatus>("status", 20)
    val verdict = enumerationByName<SubmissionVerdict>("verdict", 30).nullable()
}
