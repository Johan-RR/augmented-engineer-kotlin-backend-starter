package com.it.exalt.domain.order

class PlaceOrderUseCaseImpl(private val repository: StockArticleRepository) : PlaceOrderUseCase {

    private val stockValidator = StockValidator(repository)

    override fun execute(cmd: PlaceOrderCommand): PlaceOrderResult {
        if (cmd.items.isEmpty()) {
            throw InvalidOrderRequestException("articles cannot be empty")
        }

        stockValidator.validate(cmd.items)

        // All available: decrement and persist
        for (item in cmd.items) {
            val art = repository.findById(item.articleId)!!
            art.quantity = art.quantity - item.quantity
            repository.save(art)
        }

        return PlaceOrderResult(orderId = "order-1", status = OrderStatus.EN_ATTENTE)
    }
}
