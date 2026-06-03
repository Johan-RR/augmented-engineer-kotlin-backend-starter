package com.it.exalt.domain.order

import com.it.exalt.domain.order.model.FestivalierBalance
import com.it.exalt.domain.order.port.output.FestivalierRepository

class InMemoryFestivalierRepository : FestivalierRepository {
    private val store = mutableMapOf<String, FestivalierBalance>()

    fun seed(balance: FestivalierBalance) {
        store[balance.id] = balance
    }

    fun find(festivalierId: String): FestivalierBalance? = store[festivalierId]

    override fun findById(festivalierId: String): FestivalierBalance? = store[festivalierId]

    override fun save(balance: FestivalierBalance) {
        store[balance.id] = balance
    }
}
