package com.it.exalt.domain.order

/**
 * Holds the refundable token balance for a festivalier.
 */
data class FestivalierBalance(
    val id: String,
    var drinkTokens: Int,
    var foodTokens: Int
)

/**
 * Port for retrieving and persisting festivalier token balances.
 */
interface FestivalierRepository {
    fun findById(festivalierId: String): FestivalierBalance?
    fun save(balance: FestivalierBalance)
}
