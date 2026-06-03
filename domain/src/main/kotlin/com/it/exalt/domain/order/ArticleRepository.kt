package com.it.exalt.domain.order

interface ArticleRepository : StockArticleRepository {
    fun changeStatus(id: String, status: OrderStatus)
    // Find orders (represented as Articles in the current model) for a given festivalier
    // and status. Production adapters may implement a proper OrderRepository later.
    fun findByFestivalierIdAndStatus(festivalierId: String, status: OrderStatus): List<Article>
}
