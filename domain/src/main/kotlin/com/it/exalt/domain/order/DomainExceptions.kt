package com.it.exalt.domain.order

class StockInsufficientException(message: String = "STOCK_INSUFFISANT") : IllegalStateException(message)

class ArticleNotFoundException(message: String = "ARTICLE_NOT_FOUND") : IllegalStateException(message)

class InvalidOrderRequestException(message: String = "INVALID_ORDER_REQUEST") : IllegalArgumentException(message)

class OrderNotFoundException(message: String = "ORDER_NOT_FOUND") : IllegalArgumentException(message)

class OrderCancellationNotAllowedException(message: String = "ORDER_CANCELLATION_NOT_ALLOWED") : IllegalStateException(message)
