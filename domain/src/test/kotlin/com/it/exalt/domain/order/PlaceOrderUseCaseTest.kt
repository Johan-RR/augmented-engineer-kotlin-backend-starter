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

    @Test
    fun shouldCreateOrderWithStatusEnAttente_andDecrementStock_whenOrderingTwoAvailableMojitos() {
        // Given a festivalier identified
        val customer = fixture.identifiedFestivalier()

        // And an article "Mojito" available in stock with quantity 10
        val mojito = fixture.availableArticle("Mojito", quantity = 10)

        val handler: PlaceOrderUseCase = fixture.useCaseHandler()

        // When the festivalier places an order for 2 "Mojito"
        val command = PlaceOrderCommand(customer.id, listOf(OrderItem(mojito.id, 2)))
        val result = handler.execute(command)

        // Then the order is created with status "EN_ATTENTE" and an order id is returned
        assertThat(result.orderId).isNotNull()
        assertThat(result.status).isEqualTo(OrderStatus.EN_ATTENTE)

        // And the stock of "Mojito" is decremented by 2
        // This should fail currently because production implementation is missing.
        assertThat(mojito.quantity).isEqualTo(8)

        // TODO: implement PlaceOrderUseCase.execute to decrement Article.quantity and persist changes
    }

    @Test
    fun shouldRefuseOrder_whenStockIsInsufficient() {
        // Given an identified festivalier
        val customer = fixture.identifiedFestivalier()

        // And an article "Mojito" available in stock with quantity 1
        val mojito = fixture.availableArticle("Mojito", quantity = 1)

        val handler: PlaceOrderUseCase = fixture.useCaseHandler()

        // When attempting to create an order for 2 "Mojito"
        val command = PlaceOrderCommand(customer.id, listOf(OrderItem(mojito.id, 2)))
        val caught = try {
            handler.execute(command)
            null
        } catch (e: Exception) {
            e
        }

        // Then the order is refused and a STOCK_INSUFFISANT error is raised
        assertThat(caught).isNotNull()
        assertThat(caught!!.message).isEqualTo("STOCK_INSUFFISANT")

        // And the stock of "Mojito" is unchanged
        assertThat(mojito.quantity).isEqualTo(1)

        // TODO: implement PlaceOrderUseCase.execute to:
        //  - validate stock availability and throw IllegalStateException("STOCK_INSUFFISANT")
        //  - avoid mutating stock on failure
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


