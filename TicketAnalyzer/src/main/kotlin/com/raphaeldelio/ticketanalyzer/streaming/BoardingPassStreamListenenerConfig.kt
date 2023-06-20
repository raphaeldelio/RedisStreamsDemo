package com.raphaeldelio.ticketanalyzer.streaming

import com.raphaeldelio.ticketanalyzer.analysis.BoardingPassAnalysisService
import com.raphaeldelio.ticketanalyzer.model.BoardingPass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.RedisSystemException
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.stream.*
import org.springframework.data.redis.stream.StreamMessageListenerContainer
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions
import org.springframework.data.redis.stream.Subscription
import org.springframework.scheduling.annotation.Scheduled
import java.net.InetAddress
import java.time.Duration


@Configuration
class BoardingPassStreamListenenerConfig(
    private val boardingPassStreamListener: BoardingPassStreamListener,
    private val redisStreamService: RedisStreamService,
    private val boardingPassService: BoardingPassAnalysisService,
    private val boardingPassStreamProperties: BoardingPassStreamProperties
) {
    private val hostname: String = InetAddress.getLocalHost().hostName

    @Bean
    fun subscription(connectionFactory: RedisConnectionFactory): Subscription? {
        createConsumerGroupIfNotExists(
            connectionFactory,
            boardingPassStreamProperties.streamKey,
            boardingPassStreamProperties.consumerGroup
        )

        val streamOffset: StreamOffset<String> = StreamOffset.create(
            boardingPassStreamProperties.streamKey,
            ReadOffset.lastConsumed()
        )

        val options = StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofMillis(100))
                .build()

        val container = StreamMessageListenerContainer
                .create(connectionFactory, options)

        val subscription = container!!.receive(
            Consumer.from(
                boardingPassStreamProperties.consumerGroup,
                hostname
            ),
            streamOffset,
            boardingPassStreamListener
        )

        container.start()
        return subscription
    }

    private fun createConsumerGroupIfNotExists(
        redisConnectionFactory: RedisConnectionFactory,
        streamKey: String,
        groupName: String
    ) {
try {
            redisConnectionFactory.connection.streamCommands()
                .xGroupCreate(
                    streamKey.toByteArray(),
                    groupName,
                    ReadOffset.from("0-0"),
                    true
                )
        } catch (e: RedisSystemException) {
            println("Consumer group already exists")
        }
    }

    @Scheduled(cron = "\${boarding-pass.pending-messages.check-interval}")
    fun claimPendingMessages() {
        val pendingMessagesSummary = redisStreamService.getPendingMessages(
            boardingPassStreamProperties.streamKey,
            boardingPassStreamProperties.consumerGroup
        )

        pendingMessagesSummary?.pendingMessagesPerConsumer?.forEach { (consumerName) ->
            if (consumerName != hostname) {
                val pendingMessages = redisStreamService.getPendingMessagesPerConsumer(
                    boardingPassStreamProperties.streamKey,
                    boardingPassStreamProperties.consumerGroup,
                    consumerName
                )

                pendingMessages.forEach { pendingMessage ->
                    val messages = redisStreamService.claimMessage(
                        boardingPassStreamProperties.streamKey,
                        boardingPassStreamProperties.consumerGroup,
                        hostname,
                        boardingPassStreamProperties.pendingMessages.timeout.toLong(),
                        pendingMessage
                    )

                    messages.forEach { record ->
                        val boardingPass = BoardingPass(record.id.toString(), record.value)

                        boardingPassService.processBoardingPass(boardingPass)

                        redisStreamService.acknowledge(boardingPassStreamProperties.consumerGroup, record)
                    }
                }
            }
        }
    }
}

