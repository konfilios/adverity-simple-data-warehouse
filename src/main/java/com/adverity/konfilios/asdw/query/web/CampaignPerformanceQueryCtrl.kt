package com.adverity.konfilios.asdw.query.web

import com.adverity.konfilios.asdw.query.services.CampaignPerformanceQuery
import com.adverity.konfilios.asdw.query.services.CampaignPerformanceQuerySrv
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

/**
 * Exposes [CampaignPerformanceQuerySrv.executeQuery] via HTTP GET.
 */
@RestController
@RequestMapping("/campaignPerformance")
class CampaignPerformanceQueryCtrl(
    private val campaignPerformanceQuerySrv: CampaignPerformanceQuerySrv
) {

    @GetMapping
    fun executeQuery(
        @RequestParam(required = false, defaultValue = "")
        metrics: List<String>,

        @RequestParam(required = false, defaultValue = "")
        aggregates: List<String>,

        @RequestParam(required = false, defaultValue = "")
        sorts: List<String>,

        @RequestParam(required = false)
        offset: Int?,

        @RequestParam(required = false)
        limit: Int?,

        @RequestParam(required = false, defaultValue = "")
        filterDatasources: List<String>,

        @RequestParam(required = false, defaultValue = "")
        filterCampaigns: List<String>,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        filterDateStart: LocalDate?,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        filterDateEnd: LocalDate?
    ) =
        campaignPerformanceQuerySrv.executeQuery(
            CampaignPerformanceQuery(
                metrics = metrics,
                aggregates = aggregates,
                sorts = sorts,
                offset = offset,
                limit = limit,
                filterDatasources = filterDatasources,
                filterCampaigns = filterCampaigns,
                filterDateStart = filterDateStart,
                filterDateEnd = filterDateEnd
            )
        )

}