package com.it.exalt.domain.order

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

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
