package com.ticketscanner.ticketscanner

import com.github.javafaker.Faker
import com.ticketscanner.ticketscanner.model.BoardingPass
import com.ticketscanner.ticketscanner.model.Flight
import org.springframework.stereotype.Service
import java.util.*

@Service
class FakeBoardingPassService {
    private val faker = Faker()
    private val numberOfSeatsPerFlight = 200

    fun generateFakeTickets(numberOfTickets: Int): List<BoardingPass> {
        val flights = generateFakeFlights(numberOfTickets / numberOfSeatsPerFlight)
        val seatCount = initializeSeatCount(flights)

        return generateFakeBoardingPasses(numberOfTickets, flights, seatCount)
    }

    private fun initializeSeatCount(flights: List<Flight>) =
        flights.associateBy({ it.flightNumber }, { numberOfSeatsPerFlight })

    private fun generateFakeBoardingPasses(
        numberOfTickets: Int,
        flights: List<Flight>,
        seatCount: Map<String, Int>
    ): List<BoardingPass> {
        val mutableFlights = flights.toMutableList()
        val mutableSeatCount = seatCount.toMutableMap()

        return (1..numberOfTickets).mapNotNull {
            if (flights.isEmpty()) {
                println("No more flights available: ${it - 1} tickets generated}")
                return@mapNotNull null
            }

            val flight = mutableFlights.shuffled().first()

            val boardingPass = BoardingPass(
                passengerName = faker.name().fullName(),
                flightNumber = flight.flightNumber,
                departureAirportCode = flight.departureAirportCode,
                departureTime = flight.departureTime,
                arrivalAirportCode = flight.arrivalAirportCode,
                arrivalTime = flight.arrivalTime,
                gateNumber = flight.gateNumber,
                seatNumber = faker.bothify("?#"),
                boardingGroup = faker.numerify("Group #")
            )

            // Decrease the available seats count for the flight
            mutableSeatCount[flight.flightNumber] = seatCount[flight.flightNumber]?.minus(1) ?: 0

            if (seatCount[flight.flightNumber] == 0) {
                mutableFlights.remove(flight)
            }

            boardingPass
        }
    }

    private fun generateFakeFlights(
        numberOfFlights: Int,
    ): List<Flight> {
        val airports = (1..10).map { faker.aviation().airport() }
        val departureTimes = (1..10).map { generateRandomTime(1000) }
        val arrivalTimes = (1..10).map { generateRandomTime(2000) }

        val flights = (0 until numberOfFlights).map {
            val airportIndex = (it % 10)
            val departureTimeIndex = (it % 10)
            val arrivalTimeIndex = (it % 10)
            val flightNumber = faker.bothify("??####")
            val gateNumber = faker.bothify("?##")

            Flight(
                flightNumber = flightNumber,
                departureAirportCode = airports[airportIndex],
                departureTime = departureTimes[departureTimeIndex],
                arrivalAirportCode = airports[airportIndex],
                arrivalTime = arrivalTimes[arrivalTimeIndex],
                gateNumber = gateNumber
            )
        }
        
        return flights
    }

    private fun generateRandomTime(range: Int): Long {
        val random = Random()
        return System.currentTimeMillis() + random.nextInt(range)
    }
}