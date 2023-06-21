package com.raphaeldelio.ticketanalyzer.analysis.flightnumber

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface FlightNumberAnalysisRepository : CrudRepository<FlightNumberAnalysis, String> {
    fun findByOrderByCountDesc(pageable: Pageable?): Page<FlightNumberAnalysis?>?
}