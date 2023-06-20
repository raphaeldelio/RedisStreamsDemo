package com.raphaeldelio.ticketanalyzer.streaming

import org.springframework.data.redis.connection.stream.Consumer
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.PendingMessage
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisStreamService(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    fun getPendingMessages(streamKey: String, consumerGroup: String) =
        redisTemplate.opsForStream<Any, Any>().pending(streamKey, consumerGroup)

    fun getPendingMessagesPerConsumer(streamKey: String, consumerGroup: String, consumerName: String) =
        redisTemplate.opsForStream<Any, Any>().pending(streamKey, Consumer.from(consumerGroup, consumerName))

    fun claimMessage(
        streamKey: String,
        consumerGroup: String,
        consumer: String,
        timeout: Long,
        pendingMessage: PendingMessage,
    ): List<MapRecord<String, String, String>> {
        println("Claiming message ${pendingMessage.id}")
        return redisTemplate.opsForStream<String, String>().claim(
            streamKey,
            consumerGroup,
            consumer,
            Duration.ofMillis(timeout),
            pendingMessage.id
        )
    }

    fun acknowledge(group: String, record: MapRecord<String, String, String>) =
        redisTemplate.opsForStream<Any, Any>().acknowledge(group, record)

}