package me.suhyun.soj.domain.submission.domain.model

import java.time.LocalDate

data class DailySubmissionCount(
    val date: LocalDate,
    val count: Long
)
