package com.it.exalt.domain.order

class PlaceOrderUseCaseImpl(private val repository: ArticleRepository) : PlaceOrderUseCase {
    override fun execute(cmd: PlaceOrderCommand): PlaceOrderResult {
        if (cmd.items.isEmpty()) {
            throw InvalidOrderRequestException("articles cannot be empty")
        }
        // Validate availability without mutating state
        for (item in cmd.items) {
            val art = repository.findById(item.articleId) ?: throw ArticleNotFoundException()
            if (art.quantity < item.quantity) {
                throw StockInsufficientException()
            }
        }

        // All available: decrement and persist
        for (item in cmd.items) {
            val art = repository.findById(item.articleId)!!
            art.quantity = art.quantity - item.quantity
            repository.save(art)
        }

        return PlaceOrderResult(orderId = "order-1", status = OrderStatus.EN_ATTENTE)
    }
}
