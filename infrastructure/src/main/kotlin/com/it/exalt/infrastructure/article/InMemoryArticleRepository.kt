package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.Article
import com.it.exalt.domain.order.ArticleRepository

class InMemoryArticleRepository : ArticleRepository {
    private val storage: MutableMap<String, Article> = mutableMapOf()
    override fun findById(id: String): Article? = storage[id]
    override fun save(article: Article) { storage[article.id] = article }
}
