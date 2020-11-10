package com.adverity.konfilios.asdw

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.*

@SpringBootApplication
class AsdwWebApp {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            SpringApplication.run(AsdwWebApp::class.java, *args)
        }
    }
}