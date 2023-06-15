package com.ticketscanner.ticketscanner

import com.ticketscanner.ticketscanner.model.BoardingPass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.stream.*
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch


@Service
class BoardingPassService {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    fun addBoardingPasses(boardingPasses: List<BoardingPass>) {
        val records = boardingPasses.map {
            MapRecord.create<ByteArray, ByteArray, ByteArray>(
                "boarding-pass-stream".toByteArray(),
                it.toMap()
            )
        }

        val stopWatch = StopWatch()
        stopWatch.start()

        redisTemplate.executePipelined { connection ->
            records.forEach { connection.xAdd(it) }
            null
        }

        stopWatch.stop()
        println("Time taken to add ${boardingPasses.size} boarding passes: ${stopWatch.totalTimeMillis} ms")
    }
}

