package com.it.exalt.domain.order

import com.it.exalt.domain.order.model.ArticleType
import com.it.exalt.domain.order.model.CancelOrderCommand
import com.it.exalt.domain.order.model.FestivalierBalance
import com.it.exalt.domain.order.model.Order
import com.it.exalt.domain.order.model.OrderCancellationNotAllowedException
import com.it.exalt.domain.order.model.OrderLineItem
import com.it.exalt.domain.order.model.OrderNotFoundException
import com.it.exalt.domain.order.model.OrderStatus
import com.it.exalt.domain.order.port.input.CancelOrderUseCase
import com.it.exalt.domain.order.port.input.CancelOrderUseCaseImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class CancelOrderUseCaseTest {

    private lateinit var fixture: CancelOrderFixture

    @BeforeEach
    fun setUp() {
        fixture = CancelOrderFixture()
    }

    @Test
    fun shouldUpdateOrderStatusToAnnulee_whenCancellingPendingOrder() {
        // Given a festival goer who owns an order with status EN_ATTENTE
        val festivalier = fixture.festivalierWithTokens(drinkTokens = 0, foodTokens = 0)
        val order = fixture.pendingOrder(festivalierId = festivalier.id, items = listOf(
            CancelOrderFixture.OrderItemWithType(ArticleType.NORMAL_ALCOHOLIC, quantity = 1)
        ))
        val handler: CancelOrderUseCase = fixture.useCaseHandler()

        // When the festival goer requests the cancellation of that order
        val command = CancelOrderCommand(orderId = order.orderId, festivalierId = festivalier.id)
        val result = handler.execute(command)

        // Then the order status is updated to ANNULEE
        assertThat(fixture.findOrder(order.orderId)?.status).isEqualTo(OrderStatus.ANNULEE)

        // And a cancellation confirmation is returned
        assertThat(result).isNotNull()
    }

    @Test
    fun shouldRefundAllTokens_whenCancellingPendingOrder() {
        // Given a festival goer who owns an order with status EN_ATTENTE
        // and an initial balance of 0 tokens
        val festivalier = fixture.festivalierWithTokens(drinkTokens = 0, foodTokens = 0)
        val order = fixture.pendingOrder(festivalierId = festivalier.id, items = listOf(
            CancelOrderFixture.OrderItemWithType(ArticleType.NORMAL_ALCOHOLIC, quantity = 1)
        ))
        val handler: CancelOrderUseCase = fixture.useCaseHandler()

        // When the festival goer cancels the order (1 normal alcoholic = 1 drink token)
        val command = CancelOrderCommand(orderId = order.orderId, festivalierId = festivalier.id)
        handler.execute(command)

        // Then the festival goer's token balance is refunded with all tokens used for the order
        assertThat(fixture.findFestivalier(festivalier.id)?.drinkTokens).isEqualTo(1)
        assertThat(fixture.findFestivalier(festivalier.id)?.foodTokens).isEqualTo(0)
    }

    @Test
    fun shouldRejectCancellation_whenOrderIsAlreadyAcquittee() {
        // Given a festival goer who owns an order with status ACQUITTEE
        val festivalier = fixture.festivalierWithTokens(drinkTokens = 0, foodTokens = 0)
        val order = fixture.acknowledgedOrder(festivalierId = festivalier.id, items = listOf(
            CancelOrderFixture.OrderItemWithType(ArticleType.NORMAL_ALCOHOLIC, quantity = 1)
        ))
        val handler: CancelOrderUseCase = fixture.useCaseHandler()

        // When the festival goer requests the cancellation of that order
        val command = CancelOrderCommand(orderId = order.orderId, festivalierId = festivalier.id)
        val caught = try {
            handler.execute(command)
            null
        } catch (e: Exception) {
            e
        }

        // Then the cancellation is rejected with an OrderCancellationNotAllowedException
        assertThat(caught).isNotNull()
        assertThat(caught is OrderCancellationNotAllowedException).isEqualTo(true)

        // And the order status remains ACQUITTEE
        assertThat(fixture.findOrder(order.orderId)?.status).isEqualTo(OrderStatus.ACQUITTEE)

        // And the festival goer's token balance remains unchanged
        assertThat(fixture.findFestivalier(festivalier.id)?.drinkTokens).isEqualTo(0)
    }

    @Test
    fun shouldRefund3DrinkTokens_whenCancellingOrderWith1NormalAnd1PremiumAlcoholicDrink() {
        // Given a festival goer who placed an order for 1 normal alcoholic drink and 1 premium alcoholic drink
        // And the order has status EN_ATTENTE
        val festivalier = fixture.festivalierWithTokens(drinkTokens = 0, foodTokens = 0)
        val order = fixture.pendingOrder(festivalierId = festivalier.id, items = listOf(
            CancelOrderFixture.OrderItemWithType(ArticleType.NORMAL_ALCOHOLIC, quantity = 1),
            CancelOrderFixture.OrderItemWithType(ArticleType.PREMIUM_ALCOHOLIC, quantity = 1)
        ))
        val handler: CancelOrderUseCase = fixture.useCaseHandler()

        // When the festival goer cancels the order
        val command = CancelOrderCommand(orderId = order.orderId, festivalierId = festivalier.id)
        handler.execute(command)

        // Then 3 drink tokens are refunded to the festival goer's balance (1 for normal + 2 for premium)
        assertThat(fixture.findFestivalier(festivalier.id)?.drinkTokens).isEqualTo(3)
        assertThat(fixture.findFestivalier(festivalier.id)?.foodTokens).isEqualTo(0)
    }

    @Test
    fun shouldRefund4FoodTokens_whenCancellingOrderWith1MealAnd1Snack() {
        // Given a festival goer who placed an order for 1 meal and 1 snack
        // And the order has status EN_ATTENTE
        val festivalier = fixture.festivalierWithTokens(drinkTokens = 0, foodTokens = 0)
        val order = fixture.pendingOrder(festivalierId = festivalier.id, items = listOf(
            CancelOrderFixture.OrderItemWithType(ArticleType.MEAL, quantity = 1),
            CancelOrderFixture.OrderItemWithType(ArticleType.SNACK, quantity = 1)
        ))
        val handler: CancelOrderUseCase = fixture.useCaseHandler()

        // When the festival goer cancels the order
        val command = CancelOrderCommand(orderId = order.orderId, festivalierId = festivalier.id)
        handler.execute(command)

        // Then 4 food tokens are refunded to the festival goer's balance (3 for meal + 1 for snack)
        assertThat(fixture.findFestivalier(festivalier.id)?.drinkTokens).isEqualTo(0)
        assertThat(fixture.findFestivalier(festivalier.id)?.foodTokens).isEqualTo(4)
    }

    @Test
    fun shouldRejectCancellation_whenOrderDoesNotExist() {
        // Given an order ID that does not exist in the system
        val festivalier = fixture.festivalierWithTokens(drinkTokens = 0, foodTokens = 0)
        val handler: CancelOrderUseCase = fixture.useCaseHandler()

        // When the festival goer requests the cancellation of that order
        val command = CancelOrderCommand(orderId = "non-existent-order-id", festivalierId = festivalier.id)
        val caught = try {
            handler.execute(command)
            null
        } catch (e: Exception) {
            e
        }

        // Then the cancellation is rejected with an OrderNotFoundException
        assertThat(caught).isNotNull()
        assertThat(caught is OrderNotFoundException).isEqualTo(true)
    }
}

// ---------------------------------------------------------------------------
// Test-only helper types
// ---------------------------------------------------------------------------

data class FestivalierWithTokens(
    val id: String,
    var drinkTokens: Int,
    var foodTokens: Int
)

data class TestOrder(
    val orderId: String,
    val festivalierId: String,
    var status: OrderStatus,
    val items: List<CancelOrderFixture.OrderItemWithType> = emptyList()
)

class CancelOrderFixture {

    data class OrderItemWithType(val type: ArticleType, val quantity: Int)

    private val orderRepository = InMemoryOrderRepository()
    private val festivalierRepository = InMemoryFestivalierRepository()
    private var orderCounter = 0
    private var festivalierCounter = 0

    fun festivalierWithTokens(drinkTokens: Int, foodTokens: Int): FestivalierWithTokens {
        val id = "festivalier-${++festivalierCounter}"
        festivalierRepository.seed(FestivalierBalance(id = id, drinkTokens = drinkTokens, foodTokens = foodTokens))
        return FestivalierWithTokens(id = id, drinkTokens = drinkTokens, foodTokens = foodTokens)
    }

    fun pendingOrder(festivalierId: String, items: List<OrderItemWithType>): TestOrder {
        val id = "order-${++orderCounter}"
        val order = Order(
            id = id,
            festivalierId = festivalierId,
            status = OrderStatus.EN_ATTENTE,
            items = items.map { OrderLineItem(it.type, it.quantity) }
        )
        orderRepository.seed(order)
        return TestOrder(orderId = id, festivalierId = festivalierId, status = OrderStatus.EN_ATTENTE, items = items)
    }

    fun acknowledgedOrder(festivalierId: String, items: List<OrderItemWithType>): TestOrder {
        val id = "order-${++orderCounter}"
        val order = Order(
            id = id,
            festivalierId = festivalierId,
            status = OrderStatus.ACQUITTEE,
            items = items.map { OrderLineItem(it.type, it.quantity) }
        )
        orderRepository.seed(order)
        return TestOrder(orderId = id, festivalierId = festivalierId, status = OrderStatus.ACQUITTEE, items = items)
    }

    fun findOrder(orderId: String): TestOrder? {
        val order = orderRepository.findById(orderId) ?: return null
        return TestOrder(orderId = order.id, festivalierId = order.festivalierId, status = order.status)
    }

    fun findFestivalier(festivalierId: String): FestivalierWithTokens? {
        val balance = festivalierRepository.find(festivalierId) ?: return null
        return FestivalierWithTokens(id = balance.id, drinkTokens = balance.drinkTokens, foodTokens = balance.foodTokens)
    }

    fun useCaseHandler(): CancelOrderUseCase = CancelOrderUseCaseImpl(orderRepository, festivalierRepository)
}
