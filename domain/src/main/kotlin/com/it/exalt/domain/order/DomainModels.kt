package com.it.exalt.domain.order

data class Festivalier(val id: String)

data class Article(val id: String, val name: String, var quantity: Int, var status: OrderStatus? = null)

data class OrderItem(val articleId: String, val quantity: Int)

enum class OrderStatus { EN_ATTENTE, PRETE, ANNULEE, ACQUITTEE }

data class PlaceOrderResult(val orderId: String?, val status: OrderStatus)

class PlaceOrderCommand(val customerId: String, val items: List<OrderItem>)

interface PlaceOrderUseCase { fun execute(cmd: PlaceOrderCommand): PlaceOrderResult }

data class OrderLineItem(val type: ArticleType, val quantity: Int)

data class Order(
    val id: String,
    val festivalierId: String,
    var status: OrderStatus,
    val items: List<OrderLineItem> = emptyList()
)

data class CancelOrderCommand(val orderId: String, val festivalierId: String)

data class CancelOrderResult(val orderId: String, val status: OrderStatus)

interface CancelOrderUseCase { fun execute(command: CancelOrderCommand): CancelOrderResult }
