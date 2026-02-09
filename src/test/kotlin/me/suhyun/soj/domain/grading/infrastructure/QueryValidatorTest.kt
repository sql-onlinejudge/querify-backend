package me.suhyun.soj.domain.grading.infrastructure

import me.suhyun.soj.domain.grading.exception.GradingErrorCode
import me.suhyun.soj.global.exception.BusinessException
import org.assertj.core.api.Assertions.assertThatNoException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class QueryValidatorTest {

    private lateinit var queryValidator: QueryValidator

    @BeforeEach
    fun setUp() {
        queryValidator = QueryValidator()
    }

    @Nested
    inner class ValidQueries {

        @Test
        fun `should validate simple SELECT query`() {
            assertThatNoException().isThrownBy {
                queryValidator.validate("SELECT * FROM users")
            }
        }

        @Test
        fun `should validate SELECT with JOIN`() {
            assertThatNoException().isThrownBy {
                queryValidator.validate("SELECT u.id, o.name FROM users u JOIN orders o ON u.id = o.user_id")
            }
        }

        @Test
        fun `should validate SELECT with subquery`() {
            assertThatNoException().isThrownBy {
                queryValidator.validate("SELECT * FROM (SELECT id FROM users) AS sub")
            }
        }

        @Test
        fun `should validate SELECT with WHERE and GROUP BY`() {
            assertThatNoException().isThrownBy {
                queryValidator.validate("SELECT department, COUNT(*) FROM employees WHERE age > 30 GROUP BY department HAVING COUNT(*) > 5")
            }
        }
    }

    @Nested
    inner class InvalidSyntax {

        @Test
        fun `should throw INVALID_SQL_SYNTAX for malformed query`() {
            assertThatThrownBy { queryValidator.validate("NOT A VALID SQL!!!") }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(GradingErrorCode.INVALID_SQL_SYNTAX)
        }
    }

    @Nested
    inner class ForbiddenStatements {

        @Test
        fun `should throw FORBIDDEN_SQL_STATEMENT for INSERT`() {
            assertThatThrownBy { queryValidator.validate("INSERT INTO users (name) VALUES ('test')") }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(GradingErrorCode.FORBIDDEN_SQL_STATEMENT)
        }

        @Test
        fun `should throw FORBIDDEN_SQL_STATEMENT for UPDATE`() {
            assertThatThrownBy { queryValidator.validate("UPDATE users SET name = 'test' WHERE id = 1") }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(GradingErrorCode.FORBIDDEN_SQL_STATEMENT)
        }

        @Test
        fun `should throw FORBIDDEN_SQL_STATEMENT for DELETE`() {
            assertThatThrownBy { queryValidator.validate("DELETE FROM users WHERE id = 1") }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(GradingErrorCode.FORBIDDEN_SQL_STATEMENT)
        }

        @Test
        fun `should throw FORBIDDEN_SQL_STATEMENT for DROP`() {
            assertThatThrownBy { queryValidator.validate("DROP TABLE users") }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(GradingErrorCode.FORBIDDEN_SQL_STATEMENT)
        }

        @Test
        fun `should throw FORBIDDEN_SQL_STATEMENT for CREATE TABLE`() {
            assertThatThrownBy { queryValidator.validate("CREATE TABLE test (id INT)") }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(GradingErrorCode.FORBIDDEN_SQL_STATEMENT)
        }

        @Test
        fun `should throw FORBIDDEN_SQL_STATEMENT for ALTER TABLE`() {
            assertThatThrownBy { queryValidator.validate("ALTER TABLE users ADD COLUMN age INT") }
                .isInstanceOf(BusinessException::class.java)
                .extracting("errorCode")
                .isEqualTo(GradingErrorCode.FORBIDDEN_SQL_STATEMENT)
        }
    }
}
