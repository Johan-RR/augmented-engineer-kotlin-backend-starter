package com.it.exalt.infrastructure.article

import jakarta.persistence.Column
import jakarta.persistence.Entity
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
    var quantity: Int
)
