package com.ticketscanner.ticketscanner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TicketScannerApplication

fun main(args: Array<String>) {
	runApplication<TicketScannerApplication>(*args)
}
