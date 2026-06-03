package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.model.Article
import com.it.exalt.domain.order.model.OrderStatus
import com.it.exalt.domain.order.port.output.ArticleRepository
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

    override fun findByFestivalierIdAndStatus(festivalierId: String, status: OrderStatus): List<Article> {
        // Minimal implementation: query all article entities and filter by an ownership
        // encoding convention where the id may be prefixed with "{festivalierId}:".
        // This keeps the change small; a future refactor should introduce a proper
        // Order entity and repository with a real query.
        val entities = jpa.findAll()
        return entities.filter { e -> e.id.startsWith("$festivalierId:") && e.status == status }
            .map { it.toDomain() }
    }
}

private fun ArticleEntity.toDomain(): Article = Article(id = this.id, name = this.name, quantity = this.quantity, status = this.status)
