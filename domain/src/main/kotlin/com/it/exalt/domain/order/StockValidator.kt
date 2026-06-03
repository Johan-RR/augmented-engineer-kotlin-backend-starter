package com.it.exalt.domain.order

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
