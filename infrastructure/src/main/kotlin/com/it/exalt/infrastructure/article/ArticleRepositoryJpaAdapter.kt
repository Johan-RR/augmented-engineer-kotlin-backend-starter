package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.Article
import com.it.exalt.domain.order.ArticleRepository
import com.it.exalt.domain.order.OrderStatus
import org.springframework.stereotype.Component

@Component
class ArticleRepositoryJpaAdapter(private val jpa: SpringDataArticleRepository) : ArticleRepository {
    override fun findById(id: String): Article? {
        val entity = jpa.findById(id)
        return if (entity.isPresent) entity.get().toDomain() else null
    }

    override fun save(article: Article) {
        val entity = ArticleEntity(id = article.id, name = article.name, quantity = article.quantity, status = article.status)
        jpa.save(entity)
    }

    override fun changeStatus(id: String, status: OrderStatus) {
        val entityOpt = jpa.findById(id)
        if (entityOpt.isPresent) {
            val e = entityOpt.get()
            e.status = status
            jpa.save(e)
        } else {
            jpa.save(ArticleEntity(id = id, name = "", quantity = 0, status = status))
        }
    }
}

private fun ArticleEntity.toDomain(): Article = Article(id = this.id, name = this.name, quantity = this.quantity, status = this.status)
