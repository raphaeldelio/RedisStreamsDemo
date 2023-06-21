package com.raphaeldelio.ticketvalidator

import org.springframework.util.StopWatch

fun logTimeSpent(block: () -> Unit) {
    val stopWatch = StopWatch()
    stopWatch.start()

    block()

    stopWatch.stop()
    println("Time taken: ${stopWatch.totalTimeNanos} ns")
}
