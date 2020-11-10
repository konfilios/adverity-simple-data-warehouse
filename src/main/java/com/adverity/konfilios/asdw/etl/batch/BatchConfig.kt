package com.adverity.konfilios.asdw.etl.batch

import com.adverity.konfilios.asdw.etl.model.CampaignDailyPerformance
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * Batch job configuration.
 *
 * Orchestrates readers, writers into batch steps and jobs.
 */
@EnableBatchProcessing
@Configuration
class BatchConfig {
    /**
     * Load etl configuration properties using "asdw.etl" prefix in application.yml
     */
    @Bean
    @ConfigurationProperties("asdw.etl")
    fun etlConfigProps() =
        EtlConfigProps()

    @Bean
    fun importCampaignDailyPerformanceJob(
        jobBuilderFactory: JobBuilderFactory,
        stepBuilderFactory: StepBuilderFactory,
        csvReader: ItemReader<CampaignDailyPerformance>,
        writer: ItemWriter<CampaignDailyPerformance>,
        etlConfigProps: EtlConfigProps
    ): Job =
        jobBuilderFactory.get("importCampaignDailyPerformanceJob")
            .incrementer(RunIdIncrementer())
            .flow(
                stepBuilderFactory.get("step1")
                    .chunk<CampaignDailyPerformance, CampaignDailyPerformance>(etlConfigProps.chunkSize)
                    .reader(csvReader)
                    .writer(writer)
                    .build()
            )
            .end()
            .build()
}