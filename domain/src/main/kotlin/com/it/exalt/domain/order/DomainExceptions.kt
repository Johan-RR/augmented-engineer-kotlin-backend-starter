package com.it.exalt.domain.order

class StockInsufficientException(message: String = "STOCK_INSUFFISANT") : IllegalStateException(message)

class ArticleNotFoundException(message: String = "ARTICLE_NOT_FOUND") : IllegalStateException(message)

class InvalidOrderRequestException(message: String = "INVALID_ORDER_REQUEST") : IllegalArgumentException(message)
