package com.raphaeldelio.ticketanalyzer.analysis

import com.raphaeldelio.ticketanalyzer.analysis.model.BoardingPassAnalysis
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.util.*

interface BoardingPassAnalysisRepository : CrudRepository<BoardingPassAnalysis, String> {
    fun findByOrderByCountDesc(pageable: Pageable?): Page<BoardingPassAnalysis?>?
}