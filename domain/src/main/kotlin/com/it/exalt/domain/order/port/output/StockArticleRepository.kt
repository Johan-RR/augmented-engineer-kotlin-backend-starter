package com.it.exalt.domain.order.port.output

import com.it.exalt.domain.order.model.Article

interface StockArticleRepository {
    fun findById(id: String): Article?
    fun save(article: Article)
}
