package com.adverity.konfilios.asdw.query.services.sqlbuilder

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

/**
 * Builds sql queries and row mappers.
 *
 * This class is NOT thread safe.
 */
class SqlBuilder {
    /**
     * SQL parameters to be bound during query execution.
     */
    val sqlParams = mutableMapOf<String, Any>()

    /**
     * SQL query record offset
     */
    var offset: Int? = null

    /**
     * SQL query record limit
     */
    var limit: Int = 100

    /**
     * Fields (simple or calculated) to be included in the SELECT clause.
     */
    private val sqlSelectFieldExpressions = mutableSetOf<String>()

    /**
     * Fields to be included in the GROUP BY clause.
     */
    private val sqlGroupByFieldExpressions = mutableSetOf<String>()

    /**
     * Fields to be included in the ORDER BY clause.
     */
    private val sqlOrderByFields = mutableSetOf<SqlSort>()

    /**
     * Predicates to be included in the WHERE clause.
     */
    private val sqlWhereConditions = mutableListOf<String>()

    /**
     * Field mappers that will comprise the final row mapper.
     */
    private val resultSetFieldMappers = mutableMapOf<String, (ResultSet) -> Any>()

    /**
     * Add a SELECT field.
     */
    fun addSelect(field: SqlField) {
        sqlSelectFieldExpressions.add(field.expression)
        resultSetFieldMappers[field.name] = field.mapper
    }

    /**
     * Add a GROUP BY field.
     *
     * This automatically adds the field to SELECT.
     */
    fun addGroupBy(field: SqlField) {
        sqlGroupByFieldExpressions.add(field.expression)
        addSelect(field)
    }

    /**
     * Add a WHERE predicate.
     */
    fun addWhere(expression: String, paramName: String, paramValue: Any) {
        sqlWhereConditions.add(expression)
        sqlParams[paramName] = paramValue
    }

    /**
     * Add an ORDER BY field.
     */
    fun addOrderBy(field: SqlSort) {
        if (!field.direction.equals("asc", ignoreCase = true) &&
            !field.direction.equals("desc", ignoreCase = true)) {
            throw Exception("Unrecognized order by direction ${field.direction}")
        }
        sqlOrderByFields.add(field)
    }

    /**
     * Build SQL string
     */
    fun buildSql(): String {
        val sqlWhere: String = buildWhere()
        val sqlSelect: String = buildSelect()
        val sqlGroupBy: String = buildGroupBy()
        val sqlOrderBy: String = buildOrderBy()
        val sqlOffset: String = buildOffset()
        return """SELECT $sqlSelect 
                FROM campaign_daily_performance 
                $sqlWhere 
                $sqlGroupBy
                $sqlOrderBy
                $sqlOffset
                LIMIT $limit"""
    }

    private fun buildOrderBy() =
        if (sqlOrderByFields.isNotEmpty()) {
            " ORDER BY " + sqlOrderByFields.joinToString(", ") { "${it.column} ${it.direction}" }
        } else {
            ""
        }

    private fun buildOffset() =
        if (offset != null) {
            " OFFSET $offset"
        } else {
            ""
        }

    fun buildRowMapper(): RowMapper<Map<String, Any>> = RowMapper { rs, _ ->
        resultSetFieldMappers
            .mapValues { (_, fieldMapper) -> fieldMapper(rs) }
    }

    private fun buildGroupBy(): String =
        if (sqlGroupByFieldExpressions.isNotEmpty()) {
            " GROUP BY " + sqlGroupByFieldExpressions.joinToString(", ")
        } else {
            ""
        }

    private fun buildSelect() =
        sqlSelectFieldExpressions.joinToString(", ")

    private fun buildWhere() =
        if (sqlWhereConditions.isNotEmpty()) {
            " WHERE " + sqlWhereConditions.joinToString(" AND ")
        } else {
            ""
        }
}