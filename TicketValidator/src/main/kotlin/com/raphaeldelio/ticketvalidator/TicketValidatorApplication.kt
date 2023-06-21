package com.raphaeldelio.ticketvalidator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan
class TicketValidatorApplication

fun main(args: Array<String>) {
    runApplication<TicketValidatorApplication>(*args)
}
