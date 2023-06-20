package com.raphaeldelio.ticketanalyzer.streaming

import com.raphaeldelio.ticketanalyzer.analysis.BoardingPassAnalysisService
import com.raphaeldelio.ticketanalyzer.model.BoardingPass
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.stream.StreamListener
import org.springframework.stereotype.Service


@Service
class BoardingPassStreamListener(
    private val redisStreamService: RedisStreamService,
    private val boardingPassService: BoardingPassAnalysisService,
    private val boardingPassStreamProperties: BoardingPassStreamProperties
) : StreamListener<String, MapRecord<String, String, String>> {
    override fun onMessage(record: MapRecord<String, String, String>) {
        val boardingPass = BoardingPass(record.id.toString(), record.value)

        processMessage(boardingPass)

        redisStreamService.acknowledge(boardingPassStreamProperties.consumerGroup, record)
    }

    private fun processMessage(boardingPass: BoardingPass) {
        boardingPassService.processBoardingPass(boardingPass)
    }
}