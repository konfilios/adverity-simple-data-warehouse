package com.adverity.konfilios.asdw.query.services

import com.adverity.konfilios.asdw.query.services.sqlbuilder.SqlBuilder
import com.adverity.konfilios.asdw.query.services.sqlbuilder.SqlField
import com.adverity.konfilios.asdw.query.services.sqlbuilder.SqlSort
import mu.KotlinLogging
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

/**
 * Creates and executes an SQL query based on a [CampaignPerformanceQuery].
 */
@Service
class CampaignPerformanceQuerySrv(
    jdbcTemplate: JdbcTemplate
) {
    private val namedJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    /**
     * Creates and executes an SQL query based on a [CampaignPerformanceQuery].
     */
    fun executeQuery(query: CampaignPerformanceQuery): CampaignPeformanceQueryResult {
        val sqlBuilder = SqlBuilder()

        //
        // Quick validations
        //
        if (query.metrics.isEmpty()) {
            throw Exception("No metrics defined")
        }

        if (query.aggregates.isEmpty()) {
            //
            // No aggregates.
            // Add all dimensions.
            // Use simple field names for metrics instead of SUMs.
            //
            sqlBuilder.addSelect(FIELD_DATASOURCE)
            sqlBuilder.addSelect(FIELD_CAMPAIGN)
            sqlBuilder.addSelect(FIELD_DATE)

            if (query.metrics.contains("total_clicks")) {
                sqlBuilder.addSelect(FIELD_SIMPLE_CLICKS)
            }
            if (query.metrics.contains("total_impressions")) {
                sqlBuilder.addSelect(FIELD_SIMPLE_IMPRESSIONS)
            }
            if (query.metrics.contains("total_ctr")) {
                sqlBuilder.addSelect(FIELD_SIMPLE_CTR)
            }
        } else {
            //
            // Aggregates are present.
            // Only include aggregated dimensions.
            // Use SUM expressions in metrics.
            //
            if (query.aggregates.contains("datasource")) {
                sqlBuilder.addGroupBy(FIELD_DATASOURCE)
            }
            if (query.aggregates.contains("campaign")) {
                sqlBuilder.addGroupBy(FIELD_CAMPAIGN)
            }
            if (query.aggregates.contains("date")) {
                sqlBuilder.addGroupBy(FIELD_DATE)
            }

            if (query.metrics.contains("total_clicks")) {
                sqlBuilder.addSelect(FIELD_AGGREGATE_CLICKS)
            }
            if (query.metrics.contains("total_impressions")) {
                sqlBuilder.addSelect(FIELD_AGGREGATE_IMPRESSIONS)
            }
            if (query.metrics.contains("total_ctr")) {
                sqlBuilder.addSelect(FIELD_AGGREGATE_CTR)
            }
        }

        //
        // Set filters
        //
        if (query.filterCampaigns.isNotEmpty()) {
            sqlBuilder.addWhere("campaign IN (:campaigns)", "campaigns", query.filterCampaigns)
        }

        if (query.filterDatasources.isNotEmpty()) {
            sqlBuilder.addWhere("datasource IN (:datasources)", "datasources", query.filterDatasources)
        }

        if (query.filterDateStart != null) {
            sqlBuilder.addWhere("date >= (:dateStart)", "dateStart", query.filterDateStart)
        }

        if (query.filterDateEnd != null) {
            sqlBuilder.addWhere("date <= (:dateEnd)", "dateEnd", query.filterDateEnd)
        }

        //
        // Add ordering
        //
        query.sorts
            .map { SqlSort.parseSort(it) }
            .asSequence()
            .filterNotNull()
            .filter { it.column in SORT_FIELD_NAMES }
            .forEach { sqlBuilder.addOrderBy(it) }

        //
        // Set limit and offset
        //
        if (query.limit != null) {
            sqlBuilder.limit = query.limit
        }
        sqlBuilder.offset = query.offset

        //
        // Build and execute
        //
        val sql = sqlBuilder.buildSql()
        val rowMapper = sqlBuilder.buildRowMapper()

        logger.info { "Executing SQL: $sql" }

        return CampaignPeformanceQueryResult(
            sql = sql,
            query = query,
            records = namedJdbcTemplate.query(sql, sqlBuilder.sqlParams, rowMapper)
        )
    }

    companion object {
        val FIELD_DATASOURCE = SqlField("datasource", "datasource") { it.getString("datasource") }
        val FIELD_CAMPAIGN = SqlField("campaign", "campaign") { it.getString("campaign") }
        val FIELD_DATE = SqlField("date", "date") { it.getDate("date") }

        val FIELD_AGGREGATE_CLICKS = SqlField("total_clicks", "SUM(clicks) AS total_clicks") { it.getString("total_clicks") }
        val FIELD_AGGREGATE_IMPRESSIONS = SqlField("total_impressions", "SUM(impressions) AS total_impressions") { it.getString("total_impressions") }
        val FIELD_AGGREGATE_CTR = SqlField("total_ctr", "(CAST(SUM(clicks) AS NUMERIC)/SUM(impressions)) AS total_ctr") { it.getString("total_ctr") }

        val FIELD_SIMPLE_CLICKS = SqlField("total_clicks", "clicks AS total_clicks") { it.getString("total_clicks") }
        val FIELD_SIMPLE_IMPRESSIONS = SqlField("total_impressions", "impressions AS total_impressions") { it.getString("total_impressions") }
        val FIELD_SIMPLE_CTR = SqlField("total_ctr", "(CAST(total_clicks AS NUMERIC)/total_impressions) AS total_ctr") { it.getString("total_ctr") }

        val SORT_FIELD_NAMES = listOf(
            "datasource",
            "campaign",
            "date",
            "total_clicks",
            "total_impressions",
            "total_ctr"
        )

        val logger = KotlinLogging.logger {}
    }
}
