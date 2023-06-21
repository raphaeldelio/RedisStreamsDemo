package com.raphaeldelio.ticketvalidator.validation

import org.springframework.data.repository.CrudRepository

interface InvalidBoardingPassRepository : CrudRepository<InvalidBoardingPass, String>