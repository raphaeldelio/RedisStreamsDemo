package com.raphaeldelio.ticketvalidator.validation

import com.raphaeldelio.ticketvalidator.model.BoardingPass
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class BoardingPassValidationService(
    val invalidBoardingPassRepository: InvalidBoardingPassRepository
) {
    fun processBoardingPass(boardingPass: BoardingPass) {
        println("Received boarding pass: $boardingPass")
        runValidations(boardingPass)
    }

    private fun runValidations(boardingPass: BoardingPass) {
        if (boardingPass.flightNumber.startsWith("A")) {
            invalidBoardingPassRepository.save(
                InvalidBoardingPass(
                    boardingPass.ticketNumber,
                    "Flight number starts with A"
                )
            )
        }
    }
}