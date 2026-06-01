package com.it.exalt.application.web

import com.it.exalt.domain.order.ArticleNotFoundException
import com.it.exalt.domain.order.StockInsufficientException
import com.it.exalt.domain.order.InvalidOrderRequestException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.http.HttpStatus

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(ArticleNotFoundException::class)
    fun handleArticleNotFound(e: ArticleNotFoundException): ResponseEntity<Map<String, String?>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
    }

    @ExceptionHandler(StockInsufficientException::class)
    fun handleStockInsufficient(e: StockInsufficientException): ResponseEntity<Map<String, String?>> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to e.message))
    }

    @ExceptionHandler(InvalidOrderRequestException::class)
    fun handleInvalidOrder(e: InvalidOrderRequestException): ResponseEntity<Map<String, String?>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(e: Exception): ResponseEntity<Map<String, String?>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to e.message))
    }
}
