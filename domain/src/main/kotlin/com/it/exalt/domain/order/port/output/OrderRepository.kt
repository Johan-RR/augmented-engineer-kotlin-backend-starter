package com.it.exalt.domain.order.port.output

import com.it.exalt.domain.order.model.Order

interface OrderRepository {
    fun findById(orderId: String): Order?
    fun save(order: Order)
}
