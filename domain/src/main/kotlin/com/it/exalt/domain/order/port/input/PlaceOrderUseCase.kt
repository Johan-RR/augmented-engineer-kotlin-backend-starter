package com.it.exalt.domain.order.port.input

import com.it.exalt.domain.order.model.InvalidOrderRequestException
import com.it.exalt.domain.order.model.OrderStatus
import com.it.exalt.domain.order.model.PlaceOrderCommand
import com.it.exalt.domain.order.model.PlaceOrderResult
import com.it.exalt.domain.order.port.output.StockArticleRepository
import com.it.exalt.domain.shared.annotation.UseCase

interface PlaceOrderUseCase {
    fun execute(cmd: PlaceOrderCommand): PlaceOrderResult
}

@UseCase
class PlaceOrderUseCaseImpl(private val repository: StockArticleRepository) : PlaceOrderUseCase {

    private val stockValidator = StockValidator(repository)

    override fun execute(cmd: PlaceOrderCommand): PlaceOrderResult {
        if (cmd.items.isEmpty()) {
            throw InvalidOrderRequestException("articles cannot be empty")
        }

        stockValidator.validate(cmd.items)

        for (item in cmd.items) {
            val art = repository.findById(item.articleId)!!
            art.quantity = art.quantity - item.quantity
            repository.save(art)
        }

        return PlaceOrderResult(orderId = "order-1", status = OrderStatus.EN_ATTENTE)
    }
}
