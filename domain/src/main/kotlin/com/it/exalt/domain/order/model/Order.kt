package com.it.exalt.domain.order.model

enum class OrderStatus { EN_ATTENTE, PRETE, ANNULEE, ACQUITTEE }

data class OrderItem(val articleId: String, val quantity: Int)

data class OrderLineItem(val type: ArticleType, val quantity: Int)

data class Order(
    val id: String,
    val festivalierId: String,
    var status: OrderStatus,
    val items: List<OrderLineItem> = emptyList()
)
