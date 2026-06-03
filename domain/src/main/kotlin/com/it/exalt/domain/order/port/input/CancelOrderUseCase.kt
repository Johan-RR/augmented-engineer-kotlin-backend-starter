package com.it.exalt.domain.order.port.input

import com.it.exalt.domain.order.model.ArticleType
import com.it.exalt.domain.order.model.CancelOrderCommand
import com.it.exalt.domain.order.model.CancelOrderResult
import com.it.exalt.domain.order.model.Order
import com.it.exalt.domain.order.model.OrderCancellationNotAllowedException
import com.it.exalt.domain.order.model.OrderNotFoundException
import com.it.exalt.domain.order.model.OrderStatus
import com.it.exalt.domain.order.port.output.FestivalierRepository
import com.it.exalt.domain.order.port.output.OrderRepository

interface CancelOrderUseCase {
    fun execute(command: CancelOrderCommand): CancelOrderResult
}

class CancelOrderUseCaseImpl(
    private val orderRepository: OrderRepository,
    private val festivalierRepository: FestivalierRepository
) : CancelOrderUseCase {

    override fun execute(command: CancelOrderCommand): CancelOrderResult {
        val order = orderRepository.findById(command.orderId)
            ?: throw OrderNotFoundException()

        if (order.status == OrderStatus.ACQUITTEE) {
            throw OrderCancellationNotAllowedException()
        }

        order.status = OrderStatus.ANNULEE
        orderRepository.save(order)

        refundTokens(order)

        return CancelOrderResult(orderId = order.id, status = OrderStatus.ANNULEE)
    }

    private fun refundTokens(order: Order) {
        val balance = festivalierRepository.findById(order.festivalierId) ?: return
        for (item in order.items) {
            val (drink, food) = tokenCost(item.type)
            balance.drinkTokens += drink * item.quantity
            balance.foodTokens += food * item.quantity
        }
        festivalierRepository.save(balance)
    }

    private fun tokenCost(type: ArticleType): Pair<Int, Int> = when (type) {
        ArticleType.NORMAL_ALCOHOLIC  -> Pair(1, 0)
        ArticleType.PREMIUM_ALCOHOLIC -> Pair(2, 0)
        ArticleType.MEAL              -> Pair(0, 3)
        ArticleType.SNACK             -> Pair(0, 1)
    }
}
