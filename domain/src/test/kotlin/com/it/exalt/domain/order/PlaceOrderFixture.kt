package com.it.exalt.domain.order

import com.it.exalt.domain.order.model.Article
import com.it.exalt.domain.order.model.Festivalier
import com.it.exalt.domain.order.port.input.PlaceOrderUseCase
import com.it.exalt.domain.order.port.input.PlaceOrderUseCaseImpl

class PlaceOrderFixture {
    private val articles = mutableMapOf<String, Article>()

    fun identifiedFestivalier(): Festivalier = Festivalier("festivalier-1")

    fun availableArticle(name: String, quantity: Int): Article {
        val id = "article-${articles.size + 1}"
        val article = Article(id, name, quantity)
        articles[id] = article
        return article
    }

    fun useCaseHandler(): PlaceOrderUseCase = PlaceOrderUseCaseImpl(InMemoryArticleRepository(articles))
}
