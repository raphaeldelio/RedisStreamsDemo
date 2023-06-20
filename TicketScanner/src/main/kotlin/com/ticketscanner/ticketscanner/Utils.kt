package com.ticketscanner.ticketscanner

import org.springframework.util.StopWatch

fun logTimeSpent(block: () -> Unit) {
    val stopWatch = StopWatch()
    stopWatch.start()

    block()

    stopWatch.stop()
    println("Time taken: ${stopWatch.totalTimeMillis} ms")
}
