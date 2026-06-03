package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.model.Article
import com.it.exalt.domain.order.model.OrderStatus
import com.it.exalt.domain.order.port.output.ArticleRepository

class InMemoryArticleRepository : ArticleRepository {
    private val storage: MutableMap<String, Article> = mutableMapOf()
    override fun findById(id: String): Article? = storage[id]
    override fun save(article: Article) { storage[article.id] = article }

    override fun changeStatus(id: String, status: OrderStatus) {
        val existing = storage[id]
        if (existing != null) {
            existing.status = status
        } else {
            storage[id] = Article(id = id, name = "", quantity = 0, status = status)
        }
    }

    override fun findByFestivalierIdAndStatus(festivalierId: String, status: OrderStatus): List<Article> {
        // Minimal in-memory implementation: festivalier ownership is encoded as
        // "{festivalierId}:{orderId}" in the Article.id for compatibility with tests.
        return storage.values.filter { a -> a.id.startsWith("$festivalierId:") && a.status == status }
    }
}
