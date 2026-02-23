package me.suhyun.soj.domain.run.domain.model

data class RunResult(
    val columns: List<String>,
    val rows: List<List<String>>
) {
    companion object {
        fun parse(tsv: String): RunResult {
            val lines = tsv.trim().lines().filter { it.isNotEmpty() }
            if (lines.isEmpty()) return RunResult(emptyList(), emptyList())
            val columns = lines.first().split("\t")
            val rows = lines.drop(1).map { it.split("\t") }
            return RunResult(columns, rows)
        }
    }
}
