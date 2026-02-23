package me.suhyun.soj.domain.auth.presentation.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class MergeGuestRequest(
    @field:NotNull val guestId: UUID
)
