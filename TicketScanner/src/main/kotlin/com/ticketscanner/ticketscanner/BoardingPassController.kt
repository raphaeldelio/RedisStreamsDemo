package com.ticketscanner.ticketscanner

import com.github.javafaker.Faker
import com.ticketscanner.ticketscanner.model.BoardingPass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BoardingPassController {

    @Autowired
    private lateinit var boardingPassService: BoardingPassService

    @PostMapping("/generate-tickets")
    fun generateTickets(): ResponseEntity<String> {
        val stopWatch = StopWatch()
        val faker = Faker()
        val random = Random()

        stopWatch.start()
        val airports = (1..10).map { faker.aviation().airport() }
        val departureTimes = (1..10).map { System.currentTimeMillis() + random.nextInt(1000) }
        val arrivalTimes = (1..10).map { System.currentTimeMillis() + random.nextInt(2000) }

        val flights = mutableMapOf<String, Flight>()
        val seatCount = mutableMapOf<String, Int>()
        val boardingPasses = mutableListOf<BoardingPass>()

        // Generate 1,000 flights
        for (i in 1..10000) {
            val airportIndex = (i % 10) // Cycling through the 10 airports
            val departureTimeIndex = (i % 10) // Cycling through the 10 departure times
            val arrivalTimeIndex = (i % 10) // Cycling through the 10 arrival times

            val flightNumber = faker.bothify("??####")
            val flight = Flight(
                flightNumber = flightNumber,
                departureAirportCode = airports[airportIndex],
                departureTime = departureTimes[departureTimeIndex],
                arrivalAirportCode = airports[airportIndex],
                arrivalTime = arrivalTimes[arrivalTimeIndex],
                gateNumber = faker.bothify("?##")
            )

            flights[flightNumber] = flight
            seatCount[flightNumber] = 200 // Set the initial seat count for each flight
        }

        // Generate 10,000 tickets randomly from the flights
        for (i in 1..1000000) {
            if (flights.isEmpty()) {
                println("No more flights available: ${i - 1} tickets generated}")
                break
            }

            val randomFlightIndex = random.nextInt(flights.size)
            val flightNumber = flights.keys.elementAt(randomFlightIndex)

            val boardingPass = BoardingPass(
                passengerName = faker.name().fullName(),
                flightNumber = flightNumber,
                departureAirportCode = flights[flightNumber]!!.departureAirportCode,
                departureTime = flights[flightNumber]!!.departureTime,
                arrivalAirportCode = flights[flightNumber]!!.arrivalAirportCode,
                arrivalTime = flights[flightNumber]!!.arrivalTime,
                gateNumber = flights[flightNumber]!!.gateNumber,
                seatNumber = faker.bothify("?##"),
                boardingGroup = faker.numerify("Group #")
            )

            boardingPasses.add(boardingPass)

            // Decrease the available seats count for the flight
            seatCount[flightNumber] = seatCount[flightNumber]?.minus(1) ?: 0

            // Remove the flight from the map if there are no more available seats
            if (seatCount[flightNumber] == 0) {
                flights.remove(flightNumber)
            }
        }

        boardingPassService.addBoardingPasses(boardingPasses)

        stopWatch.stop()
        println("Generated 10,000 tickets in ${stopWatch.totalTimeMillis} milliseconds")

        return ResponseEntity.status(HttpStatus.CREATED).body("Tickets generated successfully")
    }

}

data class Flight(
    val flightNumber: String,
    val departureAirportCode: String,
    val departureTime: Long,
    val arrivalAirportCode: String,
    val arrivalTime: Long,
    val gateNumber: String,
)

