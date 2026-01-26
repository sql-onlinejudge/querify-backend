package me.suhyun.soj.domain.grading.infrastructure

import me.suhyun.soj.domain.grading.exception.QueryExecutionException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.sql.Connection
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import javax.sql.DataSource

@ExtendWith(MockitoExtension::class)
class QueryExecutorTest {

    @Mock
    private lateinit var dataSource: DataSource

    @Mock
    private lateinit var connection: Connection

    @Mock
    private lateinit var statement: Statement

    @Mock
    private lateinit var resultSet: ResultSet

    @Mock
    private lateinit var metaData: ResultSetMetaData

    private lateinit var queryExecutor: QueryExecutor

    @BeforeEach
    fun setUp() {
        queryExecutor = QueryExecutor(dataSource)
    }

    @Nested
    inner class Execute {

        @Test
        fun `should execute query and return result`() {
            whenever(dataSource.connection).thenReturn(connection)
            whenever(connection.createStatement()).thenReturn(statement)
            whenever(statement.execute(any())).thenReturn(true)
            whenever(statement.executeQuery(any())).thenReturn(resultSet)
            whenever(resultSet.metaData).thenReturn(metaData)
            whenever(metaData.columnCount).thenReturn(2)
            whenever(resultSet.next()).thenReturn(true, true, false)
            whenever(resultSet.getString(1)).thenReturn("1", "2")
            whenever(resultSet.getString(2)).thenReturn("Alice", "Bob")

            val result = queryExecutor.execute(
                schemaSql = "CREATE TABLE users (id INT, name VARCHAR(100))",
                initSql = "INSERT INTO users VALUES (1, 'Alice')",
                query = "SELECT * FROM users",
                timeoutMs = 5000
            )

            assertThat(result).contains("1\tAlice")
            assertThat(result).contains("2\tBob")
        }

        @Test
        fun `should execute query without initSql`() {
            whenever(dataSource.connection).thenReturn(connection)
            whenever(connection.createStatement()).thenReturn(statement)
            whenever(statement.execute(any())).thenReturn(true)
            whenever(statement.executeQuery(any())).thenReturn(resultSet)
            whenever(resultSet.metaData).thenReturn(metaData)
            whenever(metaData.columnCount).thenReturn(1)
            whenever(resultSet.next()).thenReturn(false)

            val result = queryExecutor.execute(
                schemaSql = "CREATE TABLE empty_table (id INT)",
                initSql = null,
                query = "SELECT * FROM empty_table",
                timeoutMs = 5000
            )

            assertThat(result).isEmpty()
        }

        @Test
        fun `should handle NULL values in result`() {
            whenever(dataSource.connection).thenReturn(connection)
            whenever(connection.createStatement()).thenReturn(statement)
            whenever(statement.execute(any())).thenReturn(true)
            whenever(statement.executeQuery(any())).thenReturn(resultSet)
            whenever(resultSet.metaData).thenReturn(metaData)
            whenever(metaData.columnCount).thenReturn(2)
            whenever(resultSet.next()).thenReturn(true, false)
            whenever(resultSet.getString(1)).thenReturn("1")
            whenever(resultSet.getString(2)).thenReturn(null)

            val result = queryExecutor.execute(
                schemaSql = "CREATE TABLE users (id INT, name VARCHAR(100))",
                initSql = null,
                query = "SELECT * FROM users",
                timeoutMs = 5000
            )

            assertThat(result).contains("NULL")
        }
    }

    @Nested
    inner class Exceptions {

        @Test
        fun `should throw QueryExecutionException on SQL error`() {
            whenever(dataSource.connection).thenReturn(connection)
            whenever(connection.createStatement()).thenReturn(statement)
            whenever(statement.execute(any())).thenThrow(RuntimeException("SQL syntax error"))

            assertThatThrownBy {
                queryExecutor.execute(
                    schemaSql = "INVALID SQL",
                    initSql = null,
                    query = "SELECT * FROM users",
                    timeoutMs = 5000
                )
            }.isInstanceOf(QueryExecutionException::class.java)
        }
    }
}
