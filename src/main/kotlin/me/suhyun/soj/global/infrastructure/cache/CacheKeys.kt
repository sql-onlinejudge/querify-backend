package me.suhyun.soj.global.infrastructure.cache

import java.util.UUID

object CacheKeys {

    private const val PREFIX = "soj"

    object User {
        fun byUuid(uuid: UUID) = "$PREFIX:user:$uuid"
    }

    object Problem {
        fun byId(id: Long) = "$PREFIX:problem:$id"
    }

    object TestCase {
        fun byProblemId(problemId: Long) = "$PREFIX:testcase:$problemId"
    }
}
