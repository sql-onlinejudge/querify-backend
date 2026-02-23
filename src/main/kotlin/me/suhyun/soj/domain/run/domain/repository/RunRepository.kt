package me.suhyun.soj.domain.run.domain.repository

import me.suhyun.soj.domain.run.domain.model.Run
import me.suhyun.soj.domain.run.domain.model.enums.RunStatus

interface RunRepository {
    fun save(run: Run): Run
    fun findById(id: Long): Run?
    fun updateResult(id: Long, status: RunStatus, result: String?, errorMessage: String?)
}
