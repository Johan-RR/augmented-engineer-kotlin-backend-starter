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

        // Then the order is refused and a STOCK_INSUFFISANT error is raised, mentioning the article
        assertThat(caught).isNotNull()
        assertThat(caught!!.message).isEqualTo("STOCK_INSUFFISANT: Mojito")

        // And the stock of "Mojito" is unchanged
        assertThat(mojito.quantity).isEqualTo(1)

        // TODO: implement PlaceOrderUseCase.execute to:
        //  - validate stock availability and throw IllegalStateException("STOCK_INSUFFISANT")
        //  - avoid mutating stock on failure
    }

    @Test
    fun shouldRefuseOrder_andIndicateArticleName_whenStockIsExactlyZero() {
        // Given un article "Bière Pale Ale" avec un stock de 0 unité
        val customer = fixture.identifiedFestivalier()
        val bierePaleAle = fixture.availableArticle("Bière Pale Ale", quantity = 0)
        val handler: PlaceOrderUseCase = fixture.useCaseHandler()

        // When un client tente de commander 1 unité
        val command = PlaceOrderCommand(customer.id, listOf(OrderItem(bierePaleAle.id, 1)))
        val caught = try {
            handler.execute(command)
            null
        } catch (e: Exception) {
            e
        }

        // Then une erreur métier est levée indiquant que le stock est insuffisant
        assertThat(caught).isNotNull()
        assertThat(caught is StockInsufficientException).isEqualTo(true)
        // And l'erreur mentionne l'article concerné
        // TODO: implement StockInsufficientException to include the article name in its message
        assertThat(caught!!.message).isEqualTo("STOCK_INSUFFISANT: Bière Pale Ale")

        // And le stock de l'article reste à 0 (aucune mutation)
        assertThat(bierePaleAle.quantity).isEqualTo(0)
    }
}


