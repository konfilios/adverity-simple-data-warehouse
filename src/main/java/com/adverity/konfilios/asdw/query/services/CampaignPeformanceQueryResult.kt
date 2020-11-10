package com.adverity.konfilios.asdw.query.services

/**
 * Query result returned to our client.
 */
class CampaignPeformanceQueryResult(
    /**
     * The SQL statement executed.
     */
    val sql : String,
    /**
     * The query object used to populate the SQL statement.
     */
    val query : CampaignPerformanceQuery,
    /**
     * The records returned by the DB as a result of SQL execution.
     */
    val records : List<Any>
)