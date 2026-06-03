package com.it.exalt.domain.order.model

data class CancelOrderCommand(val orderId: String, val festivalierId: String)

data class CancelOrderResult(val orderId: String, val status: OrderStatus)
