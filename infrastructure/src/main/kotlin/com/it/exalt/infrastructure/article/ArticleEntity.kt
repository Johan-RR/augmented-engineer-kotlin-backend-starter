package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.OrderStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "articles")
data class ArticleEntity(
    @Id
    @Column(name = "id", nullable = false)
    val id: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    var status: OrderStatus? = null
)
