package com.raphaeldelio.ticketanalyzer.analysis

import com.raphaeldelio.ticketanalyzer.analysis.flightnumber.FlightNumberAnalysisRepository
import com.raphaeldelio.ticketanalyzer.analysis.flightnumber.FlightNumberAnalysis
import com.raphaeldelio.ticketanalyzer.model.BoardingPass
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class BoardingPassAnalysisService(
    val flightNumberAnalysisRepository: FlightNumberAnalysisRepository
) {
    fun processBoardingPass(boardingPass: BoardingPass) {
        println("Received boarding pass: $boardingPass")
        processFlightNumber(boardingPass)
    }

    private fun processFlightNumber(boardingPass: BoardingPass) {
        val fromDB = flightNumberAnalysisRepository.findById(boardingPass.flightNumber)
        var count = 0
        fromDB.ifPresentOrElse(
            { count = it.count + 1 },
            { count = 1 }
        )

        flightNumberAnalysisRepository.save(
            FlightNumberAnalysis(
                boardingPass.flightNumber,
                count
            )
        )
    }

    fun getTop10Flights() = flightNumberAnalysisRepository.findByOrderByCountDesc(
            Pageable.ofSize(10)
        )?.toList() ?: emptyList()
}