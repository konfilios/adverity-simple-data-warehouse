package com.adverity.konfilios.asdw.etl.batch

import com.adverity.konfilios.asdw.etl.model.CampaignDailyPerformance
import org.intellij.lang.annotations.Language
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.Date
import javax.sql.DataSource

/**
 * Database writer of campaign daily performance entries.
 */
@Configuration
class SqlWriterConfig {
    @Bean
    fun writer(
        dataSource: DataSource
    ): JdbcBatchItemWriter<CampaignDailyPerformance> =
        JdbcBatchItemWriterBuilder<CampaignDailyPerformance>()
            .sql(SQL_INSERT)
            .itemPreparedStatementSetter { item, preparedStatement ->
                with(preparedStatement) {
                    setString(1, item.datasource)
                    setString(2, item.campaignName)
                    setDate(3, Date.valueOf(item.date))
                    setInt(4, item.clicks)
                    setInt(5, item.impressions)
                    setInt(6, item.clicks)
                    setInt(7, item.impressions)
                }
            }
            .dataSource(dataSource)
            .build()

    companion object {
        @Language("sql")
        const val SQL_INSERT = """INSERT INTO campaign_daily_performance 
            (datasource, campaign, date, clicks, impressions) 
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT ON CONSTRAINT campaign_daily_performance_natural_key_unique DO UPDATE
            SET clicks=?,
                impressions=?"""
    }
}