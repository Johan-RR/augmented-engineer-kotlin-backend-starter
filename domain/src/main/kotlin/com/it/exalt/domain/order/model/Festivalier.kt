package com.it.exalt.domain.order.model

data class Festivalier(val id: String)

data class FestivalierBalance(
    val id: String,
    var drinkTokens: Int,
    var foodTokens: Int
)
