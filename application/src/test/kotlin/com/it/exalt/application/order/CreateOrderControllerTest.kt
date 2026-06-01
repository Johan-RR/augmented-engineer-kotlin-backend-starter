package com.it.exalt.application.order

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.it.exalt.domain.order.PlaceOrderUseCase
import com.it.exalt.domain.order.PlaceOrderCommand
import com.it.exalt.domain.order.PlaceOrderResult
import com.it.exalt.domain.order.OrderStatus
import org.springframework.context.ApplicationContextInitializer
import org.junit.jupiter.api.Test

// Small test-only Given/When/Then DSL to mimic RestAssured Kotlin extensions.
fun Given(block: RequestSpecification.() -> Unit): GivenDsl {
    val spec = RestAssured.given()
    spec.block()
    return GivenDsl(spec)
}


class GivenDsl(private val spec: RequestSpecification) {
    infix fun When(block: RequestSpecification.() -> Response): Response = spec.block()
}

infix fun Response.Then(block: ValidatableResponse.() -> Unit) {
    val validatable: ValidatableResponse = this.then()
    validatable.block()
}

class CreateOrderControllerTest {

    private var context: ConfigurableApplicationContext? = null

    @BeforeEach
    fun setUp() {
        // Start the production Spring Boot application for integration testing.
        // Register a test-only PlaceOrderUseCase bean programmatically to mock domain behavior.
        val app = SpringApplication(com.it.exalt.Application::class.java)
        app.addInitializers(ApplicationContextInitializer<ConfigurableApplicationContext> { ctx ->
            ctx.beanFactory.registerSingleton("placeOrderUseCase", object : PlaceOrderUseCase {
                override fun execute(cmd: PlaceOrderCommand): PlaceOrderResult {
                    return PlaceOrderResult(orderId = "order-1", status = OrderStatus.EN_ATTENTE)
                }
            })
        })
        context = app.run()
    }

    @AfterEach
    fun tearDown() {
        context?.close()
        context = null
    }

    @Test
    fun shouldCreateOrderWithStatusEnAttente_whenPostingOrderForFestivalier42() {
        // Scenario: Commande créée avec succès
        // Given un festivalier identifié avec l'id "festivalier-42"
        // And les articles suivants sont disponibles : Mojito (10), Eau plate (50)

        // When le festivalier envoie une requête POST /commandes avec :
        // { "festivalierId": "festivalier-42", "articles": [{"id": "mojito", "quantite": 2}] }

        // Then la réponse a le statut HTTP 201
        // And la réponse contient un champ "commandeId" non vide
        // And la commande est créée avec le statut "EN_ATTENTE"

        val jsonPayload = """
            { "festivalierId": "festivalier-42", "articles": [{ "id": "mojito", "quantite": 2 }] }
        """.trimIndent()

        Given {
            contentType(ContentType.JSON)
            header("Authorization", "Basic dGVzdDp0ZXN0")
            body(jsonPayload)
        } When {
            post("http://localhost:8080/commandes")
        } Then {
            statusCode(201)
            body("commandeId", not(isEmptyString()))
            body("status", equalTo("EN_ATTENTE"))
        }
    }

    @Test
    fun shouldReturn401_whenPostingOrderWithoutAuthenticatedFestivalier() {
        // Scenario: Requête refusée si le festivalier n'est pas authentifié
        // Given aucun festivalier authentifié
        // When une requête POST /commandes est envoyée
        // Then la réponse a le statut HTTP 401

        val jsonPayload = """
            { "festivalierId": "festivalier-unknown", "articles": [{ "id": "mojito", "quantite": 2 }] }
        """.trimIndent()

        Given {
            contentType(ContentType.JSON)
            body(jsonPayload)
            // Intentionally do not set any authentication header
        } When {
            post("http://localhost:8080/commandes")
        } Then {
            statusCode(401)
        }

        // TODO: enforce authentication for POST /commandes (OrderController.postOrder)
    }

    // Test-only DTOs
    data class ArticleRequest(val id: String, val quantite: Int)
    data class CreateOrderRequest(val festivalierId: String, val articles: List<ArticleRequest>)
}
