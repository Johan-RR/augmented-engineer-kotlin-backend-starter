package com.it.exalt.domain.order

class PlaceOrderUseCaseImpl(private val articles: MutableMap<String, Article>) : PlaceOrderUseCase {
    override fun execute(cmd: PlaceOrderCommand): PlaceOrderResult {
        for (item in cmd.items) {
            val art = articles[item.articleId]
            if (art != null) {
                art.quantity = art.quantity - item.quantity
            }
        }
        return PlaceOrderResult(orderId = "order-1", status = OrderStatus.EN_ATTENTE)
    }
}
