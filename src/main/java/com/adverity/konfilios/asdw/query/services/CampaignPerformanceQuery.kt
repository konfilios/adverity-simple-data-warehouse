package com.adverity.konfilios.asdw.query.services

import java.time.LocalDate

/**
 * A full query for campaign performance records.
 *
 * All parameters are optional.
 */
class CampaignPerformanceQuery(
    /**
     * Metrics to include.
     *
     * Supported metrics: clicks, impressions, ctr
     */
    val metrics : List<String> = emptyList(),
    /**
     * Dimensions to aggregate.
     *
     * Supported dimensions: datasource, campaign, date
     */
    val aggregates : List<String> = emptyList(),
    /**
     * Dimensions to sort by.
     *
     * Supported dimensions: datasource, campaign, date
     */
    val sorts : List<String> = emptyList(),
    /**
     * Restrict results to given set of datasources.
     */
    val filterDatasources : List<String> = emptyList(),
    /**
     * Restrict results to given set of campaigns.
     */
    val filterCampaigns : List<String> = emptyList(),
    /**
     * Restrict results after given date.
     */
    val filterDateStart : LocalDate? = null,
    /**
     * Restrict results before given date.
     */
    val filterDateEnd : LocalDate? = null,
    /**
     * Offset of results
     */
    val offset : Int? = null,
    /**
     * Limit number of results
     */
    val limit : Int? = null
)