package com.it.exalt.domain.order

interface ArticleRepository {
    fun findById(id: String): Article?
    fun save(article: Article)
    fun changeStatus(id: String, status: OrderStatus)
    // Find orders (represented as Articles in the current model) for a given festivalier
    // and status. Production adapters may implement a proper OrderRepository later.
    fun findByFestivalierIdAndStatus(festivalierId: String, status: OrderStatus): List<Article>
}
