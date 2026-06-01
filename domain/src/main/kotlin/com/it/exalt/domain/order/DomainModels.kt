package com.it.exalt.domain.order

data class Festivalier(val id: String)

data class Article(val id: String, val name: String, var quantity: Int)

data class OrderItem(val articleId: String, val quantity: Int)

enum class OrderStatus { EN_ATTENTE }

data class PlaceOrderResult(val orderId: String?, val status: OrderStatus)

class PlaceOrderCommand(val customerId: String, val items: List<OrderItem>)

interface PlaceOrderUseCase { fun execute(cmd: PlaceOrderCommand): PlaceOrderResult }
