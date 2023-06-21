package com.raphaeldelio.ticketanalyzer.analysis.flightnumber

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("FlightNumberAnalysis")
data class FlightNumberAnalysis(
    @Id val flightNumber: String,
    val count: Int
)