package me.suhyun.soj.domain.grading.infrastructure

import me.suhyun.soj.domain.grading.exception.GradingErrorCode
import me.suhyun.soj.global.exception.BusinessException
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.select.Select
import org.springframework.stereotype.Component

@Component
class QueryValidator {

    fun validate(query: String) {
        val statement = try {
            CCJSqlParserUtil.parse(query)
        } catch (e: Exception) {
            throw BusinessException(GradingErrorCode.INVALID_SQL_SYNTAX)
        }

        if (statement !is Select) {
            throw BusinessException(GradingErrorCode.FORBIDDEN_SQL_STATEMENT)
        }
    }
}
