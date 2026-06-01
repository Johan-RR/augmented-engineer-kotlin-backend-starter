package com.it.exalt.application.order

import com.it.exalt.domain.order.OrderStatus
import com.it.exalt.domain.order.PlaceOrderCommand
import com.it.exalt.domain.order.PlaceOrderResult
import com.it.exalt.domain.order.PlaceOrderUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidOrderRequestException(message: String?) : RuntimeException(message)

class PlaceOrderFixture {
    private val placeOrderUseCaseSpy = PlaceOrderUseCaseSpy()

    fun getUseCaseHandler(): PlaceOrderUseCase = placeOrderUseCaseSpy
    fun getUseCaseSpy(): PlaceOrderUseCaseSpy = placeOrderUseCaseSpy

    class PlaceOrderUseCaseSpy : PlaceOrderUseCase {
        val receivedCommands = mutableListOf<PlaceOrderCommand>()

        override fun execute(cmd: PlaceOrderCommand): PlaceOrderResult {
            receivedCommands.add(cmd)
            if (cmd.items.isEmpty()) throw InvalidOrderRequestException("articles cannot be empty")
            return PlaceOrderResult(orderId = "order-1", status = OrderStatus.EN_ATTENTE)
        }
    }
}
