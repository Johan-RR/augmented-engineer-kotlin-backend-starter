package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.model.OrderStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "articles")
open class ArticleEntity(
    @Id
    @Column(name = "id", nullable = false)
    var id: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    var status: OrderStatus? = null
) {
    // JPA requires a default constructor
    constructor() : this("", "", 0, null)
}
