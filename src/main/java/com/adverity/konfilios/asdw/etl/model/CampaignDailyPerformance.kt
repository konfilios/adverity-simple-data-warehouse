package com.adverity.konfilios.asdw.etl.model

import java.time.LocalDate

/**
 * DTO of performance records read from CSV and written out to database.
 */
class CampaignDailyPerformance(
    val datasource : String,
    val campaignName: String,
    val date: LocalDate,
    val clicks: Int,
    val impressions: Int
)