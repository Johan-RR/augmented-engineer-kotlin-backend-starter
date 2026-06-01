package com.it.exalt.domain.order

class StockInsufficientException(message: String = "STOCK_INSUFFISANT") : IllegalStateException(message)

class ArticleNotFoundException(message: String = "ARTICLE_NOT_FOUND") : IllegalStateException(message)
