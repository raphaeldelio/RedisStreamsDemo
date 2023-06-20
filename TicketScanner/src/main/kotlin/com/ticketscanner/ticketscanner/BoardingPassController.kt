package com.ticketscanner.ticketscanner

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BoardingPassController(
    private val boardingPassService: BoardingPassService
) {

    @PostMapping("/generate-fake-tickets")
    fun generateFakeTickets(@RequestParam pipelined: Boolean, @RequestParam numberOfTickets: Int): ResponseEntity<String> {
        boardingPassService.addFakeBoardingPasses(pipelined, numberOfTickets)
        return ResponseEntity.status(HttpStatus.CREATED).body("Fake tickets generated successfully")
    }
}


