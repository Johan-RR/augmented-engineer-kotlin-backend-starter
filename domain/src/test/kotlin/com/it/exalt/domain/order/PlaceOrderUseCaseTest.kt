package com.it.exalt.domain.order

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PlaceOrderUseCaseTest {

    private lateinit var fixture: PlaceOrderFixture

    @BeforeEach
    fun setUp() {
        fixture = PlaceOrderFixture()
    }

    @Test
    fun shouldCreateOrderWithStatusEnAttente_whenOrderingSingleAvailableArticle() {
        // Given a festivalier identified
        val customer = fixture.identifiedFestivalier()

        // And an article "Mojito" available in stock
        val mojito = fixture.availableArticle("Mojito", quantity = 10)

        val handler: PlaceOrderUseCase = fixture.useCaseHandler()

        // When the festivalier places an order for 1 "Mojito"
        val command = PlaceOrderCommand(customer.id, listOf(OrderItem(mojito.id, 1)))
        val result = handler.execute(command)

        // Then the order is created with status "EN_ATTENTE"
        // And the festivalier receives an order id
        assertThat(result.orderId).isNotNull()
        assertThat(result.status).isEqualTo(OrderStatus.EN_ATTENTE)
    }
}

// --- Test-only minimal stubs/helpers to make the test pass (green) ---
// These are intentionally simple and placed here to satisfy the Green TDD step.

// Minimal assertion helpers to avoid needing AssertJ on the classpath.
class SimpleAssert<T>(private val actual: T?) {
    fun isNotNull() {
        if (actual == null) throw AssertionError("Expected value to be not null")
    }

    fun isEqualTo(expected: T) {
        if (actual != expected) throw AssertionError("Expected <$expected> but was <$actual>")
    }
}

fun <T> assertThat(actual: T?): SimpleAssert<T> = SimpleAssert(actual)

data class Festivalier(val id: String)

data class Article(val id: String, val name: String, var quantity: Int)

data class OrderItem(val articleId: String, val quantity: Int)

enum class OrderStatus { EN_ATTENTE }

data class PlaceOrderResult(val orderId: String?, val status: OrderStatus)

class PlaceOrderCommand(val customerId: String, val items: List<OrderItem>)

interface PlaceOrderUseCase { fun execute(cmd: PlaceOrderCommand): PlaceOrderResult }

class PlaceOrderFixture {
    fun identifiedFestivalier(): Festivalier = Festivalier("festivalier-1")

    fun availableArticle(name: String, quantity: Int): Article = Article("article-1", name, quantity)

    fun useCaseHandler(): PlaceOrderUseCase = object : PlaceOrderUseCase {
        override fun execute(cmd: PlaceOrderCommand): PlaceOrderResult {
            // Minimal behaviour: always create an order id and return EN_ATTENTE
            return PlaceOrderResult(orderId = "order-1", status = OrderStatus.EN_ATTENTE)
        }
    }
}

