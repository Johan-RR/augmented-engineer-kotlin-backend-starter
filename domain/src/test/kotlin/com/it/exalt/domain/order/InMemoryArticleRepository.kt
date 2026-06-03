package com.it.exalt.domain.order

class InMemoryArticleRepository(private val storage: MutableMap<String, Article>) : ArticleRepository {
    override fun findById(id: String): Article? = storage[id]
    override fun save(article: Article) { storage[article.id] = article }

    override fun changeStatus(id: String, status: OrderStatus) {
        storage[id]?.status = status
    }

    override fun findByFestivalierIdAndStatus(festivalierId: String, status: OrderStatus): List<Article> {
        // The tests encode festivalier ownership in the Article.id as "{festivalierId}:{orderId}".
        return storage.values.filter { a ->
            a.id.startsWith("$festivalierId:") && a.status == status
        }
    }
}
