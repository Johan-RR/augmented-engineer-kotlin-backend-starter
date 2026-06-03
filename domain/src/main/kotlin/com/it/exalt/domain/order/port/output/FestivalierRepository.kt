package com.it.exalt.domain.order.port.output

import com.it.exalt.domain.order.model.FestivalierBalance

interface FestivalierRepository {
    fun findById(festivalierId: String): FestivalierBalance?
    fun save(balance: FestivalierBalance)
}
