package com.it.exalt.domain.order

interface StockArticleRepository {
    fun findById(id: String): Article?
    fun save(article: Article)
}
