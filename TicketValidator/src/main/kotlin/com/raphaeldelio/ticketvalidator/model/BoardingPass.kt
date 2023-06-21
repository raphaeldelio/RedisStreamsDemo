package com.raphaeldelio.ticketvalidator.model

import org.springframework.data.annotation.TypeAlias
import java.io.Serializable

@TypeAlias("BoardingPass")
data class BoardingPass (
    val recordId: String,
    val ticketNumber: String,
    val passengerName: String,
    val flightNumber: String,
    val departureAirportCode: String,
    val departureTime: Long,
    val arrivalAirportCode: String,
    val arrivalTime: Long,
    val gateNumber: String,
    val seatNumber: String,
    val boardingGroup: String
) : Serializable {
    constructor(id: String, map: Map<String, String>) : this(
        id,
        map["ticketNumber"]!!,
        map["passengerName"]!!,
        map["flightNumber"]!!,
        map["departureAirportCode"]!!,
        map["departureTime"]!!.toLong(),
        map["arrivalAirportCode"]!!,
        map["arrivalTime"]!!.toLong(),
        map["gateNumber"]!!,
        map["seatNumber"]!!,
        map["boardingGroup"]!!
    )
}
