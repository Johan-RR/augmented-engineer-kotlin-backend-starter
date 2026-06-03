package com.it.exalt.domain.order.model

data class Article(val id: String, val name: String, var quantity: Int, var status: OrderStatus? = null)
