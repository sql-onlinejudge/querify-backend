package me.suhyun.soj.domain.auth.application.service

import me.suhyun.soj.domain.submission.domain.repository.SubmissionRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val submissionRepository: SubmissionRepository
) {

    fun mergeGuestRecords(guestId: UUID, loginUserId: UUID) {
        if (guestId == loginUserId) return
        transaction {
            submissionRepository.migrateUserId(guestId, loginUserId)
        }
    }
}
