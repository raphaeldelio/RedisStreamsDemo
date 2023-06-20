package com.raphaeldelio.ticketanalyzer.streaming

import org.springframework.boot.context.properties.ConfigurationProperties
@ConfigurationProperties(prefix = "boarding-pass")
class BoardingPassStreamProperties(
    val streamKey: String,
    val consumerGroup: String,
    val pendingMessages: PendingMessagesProperties
)
class PendingMessagesProperties(
    val timeout: String
)
