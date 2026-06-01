package com.it.exalt.infrastructure.article

import com.it.exalt.domain.order.Article
import com.it.exalt.domain.order.ArticleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ArticleRepositoryIntegrationTest {

    private val articleRepository: ArticleRepository = InMemoryArticleRepository()

    @Test
    fun `should save and find article`() {
        // Given an article
        val article = Article(id = "mojito", name = "Mojito", quantity = 10)

        // When saving via repository (expected to fail because no implementation provided)
        articleRepository.save(article)

        // Then it can be retrieved
        val found = articleRepository.findById(article.id)
        assertThat(found).isNotNull()
        assertThat(found!!.name).isEqualTo("Mojito")
        assertThat(found.quantity).isEqualTo(10)
    }

    // Production InMemoryArticleRepository extracted to infrastructure/src/main/kotlin
}
