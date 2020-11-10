package com.adverity.konfilios.asdw.etl.batch

import org.springframework.core.io.Resource

/**
 * Configuration properties of ETL job.
 *
 * You may set these values in [application.yml]
 */
class EtlConfigProps {
    /**
     * Input CSV file.
     *
     * Can be any time of resource (classpath, file, etc)
     */
    lateinit var inputCsv: Resource

    /**
     * Processing chunk size.
     */
    var chunkSize : Int = 10
}