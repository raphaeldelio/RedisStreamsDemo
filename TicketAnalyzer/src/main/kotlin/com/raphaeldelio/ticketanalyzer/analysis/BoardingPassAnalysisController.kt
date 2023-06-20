package com.raphaeldelio.ticketanalyzer.analysis

import com.raphaeldelio.ticketanalyzer.analysis.model.BoardingPassAnalysis
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BoardingPassAnalysisController(
    private val boardingPassAnalysisService: BoardingPassAnalysisService
) {

    @PostMapping("passengers/top-10")
    fun getTop10Passengers(): ResponseEntity<List<BoardingPassAnalysis?>> {
        return ResponseEntity.ok(boardingPassAnalysisService.getTop10Passengers())
    }
}