package com.it.exalt.domain.order.model

data class PlaceOrderCommand(val customerId: String, val items: List<OrderItem>)

data class PlaceOrderResult(val orderId: String?, val status: OrderStatus)
