package me.suhyun.soj.domain.grading.application.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ResultComparatorTest {

    private lateinit var resultComparator: ResultComparator

    @BeforeEach
    fun setUp() {
        resultComparator = ResultComparator()
    }

    @Nested
    inner class OrderSensitive {

        @Test
        fun `should return true when results match exactly`() {
            val actual = "1\tAlice\n2\tBob\n3\tCharlie"
            val expected = "1\tAlice\n2\tBob\n3\tCharlie"

            val result = resultComparator.compare(actual, expected, isOrderSensitive = true)

            assertThat(result).isTrue()
        }

        @Test
        fun `should return false when order differs`() {
            val actual = "2\tBob\n1\tAlice\n3\tCharlie"
            val expected = "1\tAlice\n2\tBob\n3\tCharlie"

            val result = resultComparator.compare(actual, expected, isOrderSensitive = true)

            assertThat(result).isFalse()
        }
    }

    @Nested
    inner class OrderInsensitive {

        @Test
        fun `should return true when results match regardless of order`() {
            val actual = "3\tCharlie\n1\tAlice\n2\tBob"
            val expected = "1\tAlice\n2\tBob\n3\tCharlie"

            val result = resultComparator.compare(actual, expected, isOrderSensitive = false)

            assertThat(result).isTrue()
        }
    }

    @Nested
    inner class EdgeCases {

        @Test
        fun `should return false when results differ`() {
            val actual = "1\tAlice\n2\tBob"
            val expected = "1\tAlice\n2\tCharlie"

            val result = resultComparator.compare(actual, expected, isOrderSensitive = true)

            assertThat(result).isFalse()
        }

        @Test
        fun `should return false when row count differs`() {
            val actual = "1\tAlice\n2\tBob"
            val expected = "1\tAlice\n2\tBob\n3\tCharlie"

            val result = resultComparator.compare(actual, expected, isOrderSensitive = false)

            assertThat(result).isFalse()
        }

        @Test
        fun `should return true when both results are empty`() {
            val result = resultComparator.compare("", "", isOrderSensitive = true)

            assertThat(result).isTrue()
        }

        @Test
        fun `should return true when both results are blank`() {
            val result = resultComparator.compare("   ", "  \n  ", isOrderSensitive = true)

            assertThat(result).isTrue()
        }

        @Test
        fun `should handle whitespace trimming`() {
            val actual = "  1\tAlice  \n  2\tBob  "
            val expected = "1\tAlice\n2\tBob"

            val result = resultComparator.compare(actual, expected, isOrderSensitive = true)

            assertThat(result).isTrue()
        }

        @Test
        fun `should return false when actual is empty but expected is not`() {
            val actual = ""
            val expected = "1\tAlice"

            val result = resultComparator.compare(actual, expected, isOrderSensitive = true)

            assertThat(result).isFalse()
        }
    }
}
