package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.Article
import com.it.exalt.domain.order.ArticleRepository
import com.it.exalt.domain.order.OrderStatus

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
}
