package me.suhyun.soj.domain.testcase.domain.model

data class InitMetadata(
    val statements: List<InsertStatement>
)

data class InsertStatement(
    val table: String,
    val rows: List<Map<String, Any?>>
)
