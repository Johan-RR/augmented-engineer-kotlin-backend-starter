package com.it.exalt.domain.order

class InMemoryArticleRepository(private val storage: MutableMap<String, Article>) : ArticleRepository {
    override fun findById(id: String): Article? = storage[id]
    override fun save(article: Article) { storage[article.id] = article }
}
