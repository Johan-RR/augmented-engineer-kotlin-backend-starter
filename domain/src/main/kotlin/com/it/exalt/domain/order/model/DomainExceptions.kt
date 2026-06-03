package com.it.exalt.domain.order.model

sealed class OrderDomainException(message: String) : RuntimeException(message)

class StockInsufficientException(articleName: String) :
    OrderDomainException("STOCK_INSUFFISANT: $articleName")

class ArticleNotFoundException(message: String = "ARTICLE_NOT_FOUND") :
    OrderDomainException(message)

class InvalidOrderRequestException(message: String = "INVALID_ORDER_REQUEST") :
    OrderDomainException(message)

class OrderNotFoundException(message: String = "ORDER_NOT_FOUND") :
    OrderDomainException(message)

class OrderCancellationNotAllowedException(message: String = "ORDER_CANCELLATION_NOT_ALLOWED") :
    OrderDomainException(message)
