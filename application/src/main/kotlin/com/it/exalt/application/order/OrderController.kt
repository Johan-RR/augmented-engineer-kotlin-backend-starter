package com.it.exalt.application.order

import com.it.exalt.domain.order.PlaceOrderCommand
import com.it.exalt.domain.order.PlaceOrderUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

data class CreateOrderResponse(val commandeId: String, val status: String)
data class CreateOrderRequest(val festivalierId: String, val articles: List<ArticleRequest>)
data class ArticleRequest(val id: String, val quantite: Int)

@RestController
class OrderController(private val placeOrderUseCase: PlaceOrderUseCase) {

    @PostMapping("/commandes")
    fun postOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<CreateOrderResponse> {
        val items = request.articles.map { com.it.exalt.domain.order.OrderItem(it.id, it.quantite) }
        val cmd = PlaceOrderCommand(request.festivalierId, items)
        val result = placeOrderUseCase.execute(cmd)
        val orderId = result.orderId ?: UUID.randomUUID().toString()
        val response = CreateOrderResponse(commandeId = orderId, status = result.status.name)
        return ResponseEntity.status(201).body(response)
    }
}
