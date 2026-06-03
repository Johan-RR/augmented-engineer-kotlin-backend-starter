package com.it.exalt.domain.order

import com.it.exalt.domain.order.model.Order
import com.it.exalt.domain.order.port.output.OrderRepository

class InMemoryOrderRepository : OrderRepository {
    private val store = mutableMapOf<String, Order>()

    fun seed(order: Order) {
        store[order.id] = order
    }

    override fun findById(orderId: String): Order? = store[orderId]

    override fun save(order: Order) {
        store[order.id] = order
    }
}
