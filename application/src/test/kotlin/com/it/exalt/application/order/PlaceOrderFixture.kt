package com.it.exalt.application.order

import com.it.exalt.domain.order.Article
import com.it.exalt.domain.order.ArticleRepository
import com.it.exalt.domain.order.PlaceOrderUseCase
import com.it.exalt.domain.order.PlaceOrderUseCaseImpl

class PlaceOrderFixture {
    fun getUseCaseHandler(): PlaceOrderUseCase {
        val initial = mutableMapOf<String, Article>()
        initial["mojito"] = Article("mojito", "Mojito", 10)
        initial["eau"] = Article("eau", "Eau plate", 50)
        return PlaceOrderUseCaseImpl(InMemoryArticleRepository(initial))
    }

    class InMemoryArticleRepository(private val storage: MutableMap<String, Article>) : ArticleRepository {
        override fun findById(id: String): Article? = storage[id]
        override fun save(article: Article) { storage[article.id] = article }
    }
}
