package me.suhyun.soj.global.common.util

import me.suhyun.soj.domain.problem.domain.model.ColumnMetadata
import me.suhyun.soj.domain.problem.domain.model.SchemaMetadata
import me.suhyun.soj.domain.problem.domain.model.TableMetadata
import me.suhyun.soj.domain.testcase.domain.model.InitMetadata
import me.suhyun.soj.domain.testcase.domain.model.InsertStatement
import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.expression.LongValue
import net.sf.jsqlparser.expression.DoubleValue
import net.sf.jsqlparser.expression.StringValue
import net.sf.jsqlparser.expression.NullValue
import net.sf.jsqlparser.expression.RowConstructor
import net.sf.jsqlparser.expression.operators.relational.ExpressionList
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.create.table.CreateTable
import net.sf.jsqlparser.statement.insert.Insert
import org.springframework.stereotype.Component

@Component
class SqlMetadataParser {

    fun parseSchema(schemaSql: String): SchemaMetadata {
        val statements = CCJSqlParserUtil.parseStatements(schemaSql)
        val tables = statements.statements
            .filterIsInstance<CreateTable>()
            .map { parseCreateTable(it) }
        return SchemaMetadata(tables)
    }

    private fun parseCreateTable(createTable: CreateTable): TableMetadata {
        val tableName = createTable.table.name
        val columns = createTable.columnDefinitions?.map { colDef ->
            val constraints = mutableListOf<String>()
            colDef.columnSpecs?.forEach { spec ->
                constraints.add(spec.uppercase())
            }
            ColumnMetadata(
                name = colDef.columnName,
                type = colDef.colDataType.dataType.uppercase() +
                    (colDef.colDataType.argumentsStringList?.let { "(${it.joinToString(", ")})" } ?: ""),
                constraints = constraints
            )
        } ?: emptyList()
        return TableMetadata(name = tableName, columns = columns)
    }

    fun parseInit(initSql: String?): InitMetadata? {
        if (initSql.isNullOrBlank()) return null
        val statements = CCJSqlParserUtil.parseStatements(initSql)
        val inserts = statements.statements
            .filterIsInstance<Insert>()
            .map { parseInsert(it) }
        return if (inserts.isEmpty()) null else InitMetadata(inserts)
    }

    private fun parseInsert(insert: Insert): InsertStatement {
        val tableName = insert.table.name
        val columnNames = insert.columns?.map { it.columnName } ?: emptyList()
        val rows = mutableListOf<Map<String, Any?>>()

        insert.values?.let { values ->
            values.expressions.forEach { expr ->
                val row = mutableMapOf<String, Any?>()
                when (expr) {
                    is RowConstructor<*> -> {
                        expr.forEach { value ->
                            val idx = expr.indexOf(value)
                            val colName = columnNames.getOrElse(idx) { "col$idx" }
                            row[colName] = extractValue(value as Expression)
                        }
                    }
                    is ExpressionList<*> -> {
                        expr.forEachIndexed { idx, value ->
                            val colName = columnNames.getOrElse(idx) { "col$idx" }
                            row[colName] = extractValue(value as Expression)
                        }
                    }
                    else -> {
                        if (columnNames.isNotEmpty()) {
                            row[columnNames[0]] = extractValue(expr)
                        }
                    }
                }
                if (row.isNotEmpty()) rows.add(row)
            }
        }

        return InsertStatement(table = tableName, rows = rows)
    }

    private fun extractValue(expr: Expression): Any? {
        return when (expr) {
            is LongValue -> expr.value
            is DoubleValue -> expr.value
            is StringValue -> expr.value
            is NullValue -> null
            else -> expr.toString()
        }
    }
}
