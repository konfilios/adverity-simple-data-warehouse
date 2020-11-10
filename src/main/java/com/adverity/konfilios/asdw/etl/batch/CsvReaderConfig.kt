package com.adverity.konfilios.asdw.etl.batch

import com.adverity.konfilios.asdw.etl.model.CampaignDailyPerformance
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * CSV reader of campaign daily performance records.
 */
@Configuration
class CsvReaderConfig {
    @Bean
    fun csvReader(
        etlConfigProps : EtlConfigProps
    ): FlatFileItemReader<CampaignDailyPerformance> =
        FlatFileItemReaderBuilder<CampaignDailyPerformance>()
            .name("campaignDailyPerformanceItemReader")
            .linesToSkip(1)
            .resource(etlConfigProps.inputCsv)
            .encoding("utf-8")
            .delimited()
            .names("datasource", "campaignName", "date", "clicks", "impressions")
            .fieldSetMapper {
                CampaignDailyPerformance(
                    datasource = it.readString(0),
                    campaignName = it.readString(1),
                    date = LocalDate.parse(it.readString(2), CSV_DATE_FORMATTER),
                    clicks = it.readInt(3),
                    impressions = it.readInt(4)
                )
            }
            .build()

    companion object {
        val CSV_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy")
    }
}