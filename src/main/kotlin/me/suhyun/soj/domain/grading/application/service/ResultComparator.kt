package me.suhyun.soj.domain.grading.application.service

import org.springframework.stereotype.Component

@Component
class ResultComparator {

    fun compare(actual: String, expected: String, isOrderSensitive: Boolean): Boolean {
        if (actual.isBlank() && expected.isBlank()) {
            return true
        }

        val actualRows = parseRows(actual)
        val expectedRows = parseRows(expected)

        if (actualRows.size != expectedRows.size) {
            return false
        }

        return if (isOrderSensitive) {
            actualRows == expectedRows
        } else {
            actualRows.sorted() == expectedRows.sorted()
        }
    }

    private fun parseRows(result: String): List<String> {
        return result.trim()
            .lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }
}
