package com.it.exalt.application.order

import com.it.exalt.domain.order.model.Article
import com.it.exalt.domain.order.model.OrderStatus
import com.it.exalt.domain.order.port.input.PlaceOrderUseCase
import com.it.exalt.domain.order.port.input.PlaceOrderUseCaseImpl
import com.it.exalt.domain.order.port.output.ArticleRepository

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
        override fun changeStatus(id: String, status: OrderStatus) {
            storage[id]?.status = status
        }

        override fun findByFestivalierIdAndStatus(festivalierId: String, status: OrderStatus): List<Article> {
            return storage.values.filter { a -> a.id.startsWith("$festivalierId:") && a.status == status }
        }
    }
}
