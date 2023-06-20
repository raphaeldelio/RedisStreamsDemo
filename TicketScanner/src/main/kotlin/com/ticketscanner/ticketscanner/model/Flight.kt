package com.ticketscanner.ticketscanner.model

data class Flight(
    val flightNumber: String,
    val departureAirportCode: String,
    val departureTime: Long,
    val arrivalAirportCode: String,
    val arrivalTime: Long,
    val gateNumber: String
)
