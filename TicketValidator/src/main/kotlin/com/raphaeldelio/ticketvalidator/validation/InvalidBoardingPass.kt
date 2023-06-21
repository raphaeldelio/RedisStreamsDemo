package com.raphaeldelio.ticketvalidator.validation

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("InvalidBoardingPass")
data class InvalidBoardingPass(
    @Id val ticketNumber: String,
    val reason: String,
)