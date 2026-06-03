package com.it.exalt.domain.order.port.input

import com.it.exalt.domain.order.model.ArticleNotFoundException
import com.it.exalt.domain.order.model.OrderItem
import com.it.exalt.domain.order.model.StockInsufficientException
import com.it.exalt.domain.order.port.output.StockArticleRepository

class StockValidator(private val repository: StockArticleRepository) {

    fun validate(items: List<OrderItem>) {
        for (item in items) {
            val article = repository.findById(item.articleId) ?: throw ArticleNotFoundException()
            if (article.quantity < item.quantity) {
                throw StockInsufficientException(article.name)
            }
        }
    }
}
