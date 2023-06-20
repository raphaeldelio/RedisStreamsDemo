package com.raphaeldelio.ticketanalyzer.analysis

import com.raphaeldelio.ticketanalyzer.analysis.model.BoardingPassAnalysis
import com.raphaeldelio.ticketanalyzer.model.BoardingPass
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service


@Service
class BoardingPassAnalysisService(
    val boardingPassAnalysisRepository: BoardingPassAnalysisRepository
) {
    fun processBoardingPass(boardingPass: BoardingPass) {
        println("Received boarding pass: $boardingPass")
        val fromDB = boardingPassAnalysisRepository.findById(boardingPass.flightNumber)
        var count = 0
        fromDB.ifPresentOrElse(
            { count = it.count + 1 },
            { count = 1 }
        )

        boardingPassAnalysisRepository.save(
            BoardingPassAnalysis(
                boardingPass.flightNumber,
                count
            )
        )
    }

    fun getTop10Passengers(): List<BoardingPassAnalysis?> {
        return boardingPassAnalysisRepository.findByOrderByCountDesc(
            Pageable.ofSize(10)
        )?.toList() ?: emptyList()
    }
}