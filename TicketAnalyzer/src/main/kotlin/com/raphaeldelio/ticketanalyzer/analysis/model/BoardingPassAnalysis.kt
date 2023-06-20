package com.raphaeldelio.ticketanalyzer.analysis.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("BoardingPassAnalysis")
data class BoardingPassAnalysis(
    @Id val name: String,
    val count: Int
)