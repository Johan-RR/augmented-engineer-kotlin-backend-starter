package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.Article
import com.it.exalt.domain.order.ArticleRepository
import org.springframework.stereotype.Component

@Component
class ArticleRepositoryJpaAdapter(private val jpa: SpringDataArticleRepository) : ArticleRepository {
    override fun findById(id: String): Article? {
        val entity = jpa.findById(id)
        return if (entity.isPresent) entity.get().toDomain() else null
    }

    override fun save(article: Article) {
        val entity = ArticleEntity(id = article.id, name = article.name, quantity = article.quantity)
        jpa.save(entity)
    }
}

private fun ArticleEntity.toDomain(): Article = Article(id = this.id, name = this.name, quantity = this.quantity)
