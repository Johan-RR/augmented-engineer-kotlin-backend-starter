package com.it.exalt.domain.order.port.output

import com.it.exalt.domain.order.model.Article
import com.it.exalt.domain.order.model.OrderStatus

interface ArticleRepository : StockArticleRepository {
    fun changeStatus(id: String, status: OrderStatus)
    fun findByFestivalierIdAndStatus(festivalierId: String, status: OrderStatus): List<Article>
}
