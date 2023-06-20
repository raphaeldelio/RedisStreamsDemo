package com.ticketscanner.ticketscanner

import com.ticketscanner.ticketscanner.model.BoardingPass
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.connection.stream.StreamRecords
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class BoardingPassService(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val fakeBoardingPassService: FakeBoardingPassService
) {
    private val streamKey: String = "boarding-pass-stream"

    fun addFakeBoardingPasses(pipelined: Boolean, numberOfTickets: Int) {
        val boardingPasses = fakeBoardingPassService.generateFakeTickets(numberOfTickets)

        if (pipelined)
            addBoardingPassesPipelined(boardingPasses)
        else
            addBoardingPasses(boardingPasses)
    }

    fun addBoardingPasses(boardingPasses: List<BoardingPass>) {
        logTimeSpent {
            boardingPasses.forEach { boardingPass ->
                val record: ObjectRecord<String, BoardingPass> = StreamRecords.newRecord()
                    .ofObject<BoardingPass>(boardingPass)
                    .withStreamKey(streamKey)

                redisTemplate.opsForStream<Any, Any>()
                    .add(record)
            }
        }
    }

    fun addBoardingPassesPipelined(boardingPasses: List<BoardingPass>) {
        logTimeSpent {
            val records = boardingPasses.map {
                MapRecord.create<ByteArray, ByteArray, ByteArray>(
                    streamKey.toByteArray(),
                    it.toMap()
                )
            }

            redisTemplate.executePipelined { connection ->
                records.forEach { connection.xAdd(it) }
                null
            }
        }
    }
}

