package com.it.exalt.domain.order

/**
 * Port for persisting and retrieving Order aggregates.
 */
interface OrderRepository {
    fun findById(orderId: String): Order?
    fun save(order: Order)
}
