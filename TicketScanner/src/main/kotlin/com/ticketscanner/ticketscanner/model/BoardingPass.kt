package com.ticketscanner.ticketscanner.model

import org.springframework.data.annotation.TypeAlias
import java.io.Serializable

@TypeAlias("BoardingPass")
data class BoardingPass (
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
    fun toMap(): Map<ByteArray, ByteArray> =
        mapOf(
            "ticketNumber".toByteArray() to this.ticketNumber.toString().toByteArray(),
            "passengerName".toByteArray() to this.passengerName.toByteArray(),
            "flightNumber".toByteArray() to this.flightNumber.toByteArray(),
            "departureAirportCode".toByteArray() to this.departureAirportCode.toByteArray(),
            "departureTime".toByteArray() to this.departureTime.toString().toByteArray(),
            "arrivalAirportCode".toByteArray() to this.arrivalAirportCode.toByteArray(),
            "arrivalTime".toByteArray() to this.arrivalTime.toString().toByteArray(),
            "gateNumber".toByteArray() to this.gateNumber.toByteArray(),
            "seatNumber".toByteArray() to this.seatNumber.toByteArray(),
            "boardingGroup".toByteArray() to this.boardingGroup.toByteArray()
        )
}
