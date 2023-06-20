package com.raphaeldelio.ticketanalyzer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan
class TicketAnalyzerApplication

fun main(args: Array<String>) {
    runApplication<TicketAnalyzerApplication>(*args)
}
