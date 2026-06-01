package com.it.exalt.domain.order

interface ArticleRepository {
    fun findById(id: String): Article?
    fun save(article: Article)
    fun changeStatus(id: String, status: OrderStatus)
}
